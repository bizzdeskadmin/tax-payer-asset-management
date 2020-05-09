package com.bizzdesk.group.taxpayer.asset.management.kafka;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface AddAssetRequestChannel {

    @Output(value = "add-asset-request")
    MessageChannel output();
}
