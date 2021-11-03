# Documentation for Smart Home API

<a name="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to *https://localhost:8043*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AuthenticationControllerApi* | [**authenticate**](Apis/AuthenticationControllerApi.md#authenticate) | **POST** /api/authentication | 
*AuthenticationControllerApi* | [**get**](Apis/AuthenticationControllerApi.md#get) | **GET** /api/authentication | 
*DeviceCommandControllerApi* | [**deleteCommandHistory**](Apis/DeviceCommandControllerApi.md#deletecommandhistory) | **DELETE** /api/devices/{deviceId}/commands/history | 
*DeviceCommandControllerApi* | [**fetchCommandHistory**](Apis/DeviceCommandControllerApi.md#fetchcommandhistory) | **GET** /api/devices/{deviceId}/commands/history | 
*DeviceCommandControllerApi* | [**issueCommand**](Apis/DeviceCommandControllerApi.md#issuecommand) | **POST** /api/devices/{deviceId}/commands | 
*DeviceConfigurationControllerApi* | [**createDeviceConfiguration**](Apis/DeviceConfigurationControllerApi.md#createdeviceconfiguration) | **POST** /api/devices/{deviceId}/configurations | 
*DeviceConfigurationControllerApi* | [**deleteDeviceConfiguration**](Apis/DeviceConfigurationControllerApi.md#deletedeviceconfiguration) | **DELETE** /api/devices/{deviceId}/configurations/{configurationId} | 
*DeviceConfigurationControllerApi* | [**getDeviceConfiguration**](Apis/DeviceConfigurationControllerApi.md#getdeviceconfiguration) | **GET** /api/devices/{deviceId}/configurations/{configurationId} | 
*DeviceConfigurationControllerApi* | [**getDeviceConfigurations**](Apis/DeviceConfigurationControllerApi.md#getdeviceconfigurations) | **GET** /api/devices/{deviceId}/configurations | 
*DeviceConfigurationControllerApi* | [**updateDeviceConfiguration**](Apis/DeviceConfigurationControllerApi.md#updatedeviceconfiguration) | **PUT** /api/devices/{deviceId}/configurations/{configurationId} | 
*DeviceControllerApi* | [**createDevice**](Apis/DeviceControllerApi.md#createdevice) | **POST** /api/devices | 
*DeviceControllerApi* | [**getDevice**](Apis/DeviceControllerApi.md#getdevice) | **GET** /api/devices/{deviceId} | 
*DeviceControllerApi* | [**listDevices**](Apis/DeviceControllerApi.md#listdevices) | **GET** /api/devices | 
*DeviceControllerApi* | [**removeDevice**](Apis/DeviceControllerApi.md#removedevice) | **DELETE** /api/devices/{deviceId} | 
*DeviceControllerApi* | [**updateDevice**](Apis/DeviceControllerApi.md#updatedevice) | **PUT** /api/devices/{deviceId} | 
*DevicePropertiesControllerApi* | [**addDeviceProperty**](Apis/DevicePropertiesControllerApi.md#adddeviceproperty) | **POST** /api/devices/{deviceId}/properties | 
*DevicePropertiesControllerApi* | [**deleteProperty**](Apis/DevicePropertiesControllerApi.md#deleteproperty) | **DELETE** /api/devices/{deviceId}/properties/{propertyId} | 
*DevicePropertiesControllerApi* | [**getDeviceProperty**](Apis/DevicePropertiesControllerApi.md#getdeviceproperty) | **GET** /api/devices/{deviceId}/properties/{propertyId} | 
*DevicePropertiesControllerApi* | [**listDeviceProperties**](Apis/DevicePropertiesControllerApi.md#listdeviceproperties) | **GET** /api/devices/{deviceId}/properties | 
*DevicePropertiesControllerApi* | [**listPropertyTypes**](Apis/DevicePropertiesControllerApi.md#listpropertytypes) | **GET** /api/devices/property-types | 
*DevicePropertiesControllerApi* | [**updateDeviceProperty**](Apis/DevicePropertiesControllerApi.md#updatedeviceproperty) | **PUT** /api/devices/{deviceId}/properties/{propertyId} | 
*DeviceTelemetryControllerApi* | [**deleteRecords**](Apis/DeviceTelemetryControllerApi.md#deleterecords) | **DELETE** /api/devices/{deviceId}/properties/{propertyId}/state-history | 
*DeviceTelemetryControllerApi* | [**fetchStateHistory**](Apis/DeviceTelemetryControllerApi.md#fetchstatehistory) | **GET** /api/devices/{deviceId}/properties/{propertyId}/state-history | 
*UserControllerApi* | [**getDeviceNotificationSettings**](Apis/UserControllerApi.md#getdevicenotificationsettings) | **GET** /api/users/{userId}/{deviceId}/notification-settings | 
*UserControllerApi* | [**getNotificationSettings**](Apis/UserControllerApi.md#getnotificationsettings) | **GET** /api/users/{userId}/notification-settings | 
*UserControllerApi* | [**getUser**](Apis/UserControllerApi.md#getuser) | **GET** /api/users/{userId} | 
*UserControllerApi* | [**listUsers**](Apis/UserControllerApi.md#listusers) | **GET** /api/users | 
*UserControllerApi* | [**recoverUserAccount**](Apis/UserControllerApi.md#recoveruseraccount) | **POST** /api/users/recovery | 
*UserControllerApi* | [**registerUser**](Apis/UserControllerApi.md#registeruser) | **POST** /api/users | 
*UserControllerApi* | [**removeUser**](Apis/UserControllerApi.md#removeuser) | **DELETE** /api/users/{userId} | 
*UserControllerApi* | [**updateNotificationSettings**](Apis/UserControllerApi.md#updatenotificationsettings) | **PUT** /api/users/{userId}/notification-settings | 
*UserControllerApi* | [**updateUserDetails**](Apis/UserControllerApi.md#updateuserdetails) | **PUT** /api/users/{userId} | 


<a name="documentation-for-models"></a>
## Documentation for Models

 - [CreateUserRequest](./Models/CreateUserRequest.md)
 - [DeviceCommand](./Models/DeviceCommand.md)
 - [DeviceConfiguration](./Models/DeviceConfiguration.md)
 - [DeviceDetails](./Models/DeviceDetails.md)
 - [DeviceDetailsUpdateRequest](./Models/DeviceDetailsUpdateRequest.md)
 - [DeviceMetadata](./Models/DeviceMetadata.md)
 - [DeviceProperty](./Models/DeviceProperty.md)
 - [DeviceRegistrationRequest](./Models/DeviceRegistrationRequest.md)
 - [DeviceState](./Models/DeviceState.md)
 - [ErrorResponse](./Models/ErrorResponse.md)
 - [JwtAuthentication](./Models/JwtAuthentication.md)
 - [NetworkSettings](./Models/NetworkSettings.md)
 - [NotificationSetting](./Models/NotificationSetting.md)
 - [UserDetails](./Models/UserDetails.md)
 - [UserNotificationSettings](./Models/UserNotificationSettings.md)
 - [UsernamePasswordAuthenticationRequest](./Models/UsernamePasswordAuthenticationRequest.md)


<a name="documentation-for-authorization"></a>
## Documentation for Authorization

<a name="bearerAuth"></a>
### bearerAuth

- **Type**: HTTP basic authentication

