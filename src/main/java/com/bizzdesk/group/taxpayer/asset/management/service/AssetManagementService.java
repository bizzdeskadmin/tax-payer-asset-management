package com.bizzdesk.group.taxpayer.asset.management.service;

import com.bizzdesk.group.taxpayer.asset.management.entity.Asset;
import com.bizzdesk.group.taxpayer.asset.management.entity.AssetType;
import com.bizzdesk.group.taxpayer.asset.management.entity.Location;
import com.bizzdesk.group.taxpayer.asset.management.helper.MapperModel;
import com.bizzdesk.group.taxpayer.asset.management.kafka.AddAssetRequestChannel;
import com.bizzdesk.group.taxpayer.asset.management.kafka.AddAssetResponseChannel;
import com.bizzdesk.group.taxpayer.asset.management.repository.AssetRepository;
import com.bizzdesk.group.taxpayer.asset.management.repository.AssetTypeRepository;
import com.bizzdesk.group.taxpayer.asset.management.repository.LocationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gotax.framework.library.entity.helpers.*;
import com.gotax.framework.library.error.handling.GoTaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AssetManagementService {

    @Value("${tin.validation.url}")
    private String tinValidationUrl;

    private AssetRepository assetRepository;
    private AssetTypeRepository assetTypeRepository;
    private LocationRepository locationRepository;
    private RestTemplate restTemplate;
    private AddAssetRequestChannel addAssetRequestChannel;
    private AddAssetResponseChannel addAssetResponseChannel;

    @Autowired
    public AssetManagementService(AssetRepository assetRepository, AssetTypeRepository assetTypeRepository,
                                  LocationRepository locationRepository, RestTemplate restTemplate,
                                  AddAssetRequestChannel addAssetRequestChannel, AddAssetResponseChannel addAssetResponseChannel) {
        this.assetRepository = assetRepository;
        this.assetTypeRepository = assetTypeRepository;
        this.locationRepository = locationRepository;
        this.restTemplate = restTemplate;
        this.addAssetRequestChannel = addAssetRequestChannel;
        this.addAssetResponseChannel = addAssetResponseChannel;
    }

    public void createLocation(LocationRequest locationRequest) throws GoTaxException {
        boolean locationExist = locationRepository.existsByLocation(locationRequest.getLocation());
        if(locationExist) {
            throw new GoTaxException(MessageFormat.format("Location {0} Already Exist", locationRequest.getLocation()));
        } else {
            Location location = new Location().setLocation(locationRequest.getLocation());
            locationRepository.save(location);
        }
    }

    public List<LocationResponse> listAllLocations() {

        List<LocationResponse> locationResponseList = new ArrayList<>();
        List<Location> locationList = locationRepository.findAll();
        if(!locationList.isEmpty()) {
            locationList.forEach(location -> {
                LocationResponse locationResponse = MapperModel.mapLocationToResponse(location);
                locationResponseList.add(locationResponse);
            });
        }
        return locationResponseList;
    }

    public void createAssetType(AssetTypeRequest assetTypeRequest) throws GoTaxException {
        boolean assetTypeExist = assetTypeRepository.existsByAssetType(assetTypeRequest.getAssetType());
        if(assetTypeExist) {
            throw new GoTaxException(MessageFormat.format("Asset Type {0} Already Exist", assetTypeRequest.getAssetType()));
        } else {
            AssetType assetType = new AssetType().setAssetType(assetTypeRequest.getAssetType());
            assetTypeRepository.save(assetType);
        }
    }

    public List<AssetTypeResponse> listAssetTypes() {
        List<AssetTypeResponse> assetTypeResponseList = new ArrayList<>();
        List<AssetType> assetTypeList = assetTypeRepository.findAll();
        if(!assetTypeList.isEmpty()) {
            assetTypeList.forEach(assetType -> {
                AssetTypeResponse assetTypeResponse = MapperModel.mapAssetTypeToResponse(assetType);
                assetTypeResponseList.add(assetTypeResponse);
            });
        }
        return assetTypeResponseList;
    }

    public void createAsset(AssetRequest assetRequest) throws GoTaxException {
        Long locationId = assetRequest.getLocationId();
        Location location = locationRepository.findById(locationId).orElseThrow(
                () -> new GoTaxException(MessageFormat.format("Location ID: {0} Does Not Exist", locationId))
        );

        Long assetTypeId = assetRequest.getAssetTypeId();
        AssetType assetType = assetTypeRepository.findById(assetTypeId).orElseThrow(
                () -> new GoTaxException(MessageFormat.format("Asset Type Id: {0} Does Not Exist", assetTypeId))
        );

        String tin = assetRequest.getTin();
        if(!validateTin(tin)) {
            throw new GoTaxException(MessageFormat.format("Tin {0} Does Not Exist", tin));
        } else {
            Asset asset = new Asset().setAssetType(assetType).setAssetValue(assetRequest.getAssetValue())
                    .setDateOfPurchase(assetRequest.getDateOfPurchase())
                    .setDescription(assetRequest.getDescription())
                    .setLocation(location)
                    .setTin(tin);
            Asset createdAsset = assetRepository.save(asset);

            AssetResponse assetResponse = MapperModel.mapAssetToAssetResponse(createdAsset);
            addAssetRequestChannel.output().send(MessageBuilder.withPayload(assetResponse).build());
        }

    }

    private Boolean validateTin(String tin) {
        return restTemplate.exchange(tinValidationUrl, HttpMethod.GET, null, Boolean.class, tin).getBody();
    }

    @ServiceActivator(inputChannel = "add-asset-response")
    public void getAssetResponseMessageFromJTB(Message<String> assetResponseMessage) {
        try {
            AssetResponse assetResponse = new ObjectMapper().readValue(assetResponseMessage.getPayload(), AssetResponse.class);
            this.updateAssetWithJTBResponse(assetResponse);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void updateAssetWithJTBResponse(AssetResponse assetResponse) throws GoTaxException {
        Asset asset = assetRepository.findById(assetResponse.getId()).orElseThrow(
                () -> new GoTaxException(MessageFormat.format("Asset Detail With ID: {0} Does Not Exist", assetResponse.getId()))
        );
        asset.setJtbResponse(assetResponse.getJtbResponse())
                .setJtbResponseDescription(assetResponse.getJtbResponseDescription());
        assetRepository.save(asset);
    }
}
