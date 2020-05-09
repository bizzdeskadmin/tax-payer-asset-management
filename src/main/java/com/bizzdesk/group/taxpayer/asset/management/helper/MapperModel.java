package com.bizzdesk.group.taxpayer.asset.management.helper;

import com.bizzdesk.group.taxpayer.asset.management.entity.Asset;
import com.bizzdesk.group.taxpayer.asset.management.entity.AssetType;
import com.bizzdesk.group.taxpayer.asset.management.entity.Location;
import com.gotax.framework.library.entity.helpers.AssetResponse;
import com.gotax.framework.library.entity.helpers.AssetTypeResponse;
import com.gotax.framework.library.entity.helpers.LocationResponse;

public class MapperModel {

    public static LocationResponse mapLocationToResponse(Location location) {
        return new LocationResponse().setId(location.getId()).setLocation(location.getLocation());
    }

    public static AssetTypeResponse mapAssetTypeToResponse(AssetType assetType) {
        return new AssetTypeResponse().setAssetType(assetType.getAssetType())
                .setAssetTypeId(assetType.getAssetTypeId());
    }

    public static AssetResponse mapAssetToAssetResponse(Asset asset) {
        return new AssetResponse().setJtbResponseDescription(asset.getJtbResponseDescription())
                .setJtbResponse(asset.getJtbResponse())
                .setAssetTypeResponse(mapAssetTypeToResponse(asset.getAssetType()))
                .setAssetValue(asset.getAssetValue())
                .setDateOfPurchase(asset.getDateOfPurchase())
                .setDescription(asset.getDescription())
                .setId(asset.getId())
                .setLocationResponse(mapLocationToResponse(asset.getLocation()))
                .setTin(asset.getTin());
    }
}
