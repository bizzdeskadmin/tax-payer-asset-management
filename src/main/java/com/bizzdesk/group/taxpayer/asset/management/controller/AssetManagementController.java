package com.bizzdesk.group.taxpayer.asset.management.controller;

import com.bizzdesk.group.taxpayer.asset.management.service.AssetManagementService;
import com.gotax.framework.library.entity.helpers.*;
import com.gotax.framework.library.error.handling.GoTaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class AssetManagementController {

    private AssetManagementService assetManagementService;

    @Autowired
    public AssetManagementController(AssetManagementService assetManagementService) {
        this.assetManagementService = assetManagementService;
    }

    @PostMapping("/v1/create/location")
    public void createLocation(@Valid @RequestBody LocationRequest locationRequest) throws GoTaxException {
        assetManagementService.createLocation(locationRequest);
    }

    @PostMapping("/v1/create/asset/type")
    public void createAssetType(@Valid @RequestBody AssetTypeRequest assetTypeRequest) throws GoTaxException {
        assetManagementService.createAssetType(assetTypeRequest);
    }

    @PostMapping("/v1/create/asset")
    public void createAsset(@Valid @RequestBody AssetRequest assetRequest) throws GoTaxException {
        assetManagementService.createAsset(assetRequest);
    }

    @GetMapping("/v1/location/list")
    public List<LocationResponse> listAllLocations() {
        return assetManagementService.listAllLocations();
    }

    @GetMapping("/v1/asset/type/list")
    public List<AssetTypeResponse> listAssetTypes() {
        return assetManagementService.listAssetTypes();
    }
}
