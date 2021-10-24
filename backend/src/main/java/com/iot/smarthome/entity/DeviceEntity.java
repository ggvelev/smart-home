package com.iot.smarthome.entity;

import javax.persistence.*;
import java.util.UUID;

/**
 * Representation of a Device
 */
@Entity
@Table(name = "devices")
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private Long id;

    @Column(name = "uuid")
    private final UUID uuid;

    // TODO - separate entity with OneToMany relationship with devices, having columns {id, uuid, key, value}
    @Embedded
    @AttributeOverride(name = "locationBuilding", column = @Column(name = "loc_building"))
    @AttributeOverride(name = "locationFloor", column = @Column(name = "loc_floor"))
    @AttributeOverride(name = "locationRoom", column = @Column(name = "loc_room"))
    private DeviceMetadata metadata;

    @Embedded
    private NetworkSettings networkSettings;

    public DeviceEntity() {
        uuid = UUID.randomUUID();
    }

    /**
     * Since devices themselves have their UUIDs embedded in firmware, when registering a device and hence creating new
     * DB record, its uuid should be provided in order to know later on to which MQTT topic to publish messages
     *
     * @param uuid newly registered device UUID
     * @implNote Probably a bit ugly designed, but a general refactoring is awaiting after releasing MVP 😊
     */
    public DeviceEntity(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public DeviceMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(DeviceMetadata metadata) {
        this.metadata = metadata;
    }

    public NetworkSettings getNetworkSettings() {
        return networkSettings;
    }

    public void setNetworkSettings(NetworkSettings networkSettings) {
        this.networkSettings = networkSettings;
    }

}
