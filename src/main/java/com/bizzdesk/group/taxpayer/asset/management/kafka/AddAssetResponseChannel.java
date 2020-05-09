package com.bizzdesk.group.taxpayer.asset.management.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface AddAssetResponseChannel {

    @Input(value = "add-asset-response")
    SubscribableChannel input();
}
