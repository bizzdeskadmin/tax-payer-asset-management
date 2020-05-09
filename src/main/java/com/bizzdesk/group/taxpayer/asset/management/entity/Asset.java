package com.bizzdesk.group.taxpayer.asset.management.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tin;
    @JoinColumn(name = "location_id")
    @OneToOne
    private Location location;
    private String assetValue;
    @JoinColumn(name = "asset_id")
    @OneToOne
    private AssetType assetType;
    @Temporal(TemporalType.DATE)
    private Date dateOfPurchase;
    private String description;
    private String jtbResponse;
    private String jtbResponseDescription;
}
