# DeviceConfigurationControllerApi

All URIs are relative to *https://localhost:8043*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createDeviceConfiguration**](DeviceConfigurationControllerApi.md#createDeviceConfiguration) | **POST** /api/devices/{deviceId}/configurations | 
[**deleteDeviceConfiguration**](DeviceConfigurationControllerApi.md#deleteDeviceConfiguration) | **DELETE** /api/devices/{deviceId}/configurations/{configurationId} | 
[**getDeviceConfiguration**](DeviceConfigurationControllerApi.md#getDeviceConfiguration) | **GET** /api/devices/{deviceId}/configurations/{configurationId} | 
[**getDeviceConfigurations**](DeviceConfigurationControllerApi.md#getDeviceConfigurations) | **GET** /api/devices/{deviceId}/configurations | 
[**updateDeviceConfiguration**](DeviceConfigurationControllerApi.md#updateDeviceConfiguration) | **PUT** /api/devices/{deviceId}/configurations/{configurationId} | 


<a name="createDeviceConfiguration"></a>
# **createDeviceConfiguration**
> Object createDeviceConfiguration(deviceId, DeviceConfiguration)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceId** | **String**|  | [default to null]
 **DeviceConfiguration** | [**DeviceConfiguration**](../Models/DeviceConfiguration.md)|  |

### Return type

[**Object**](../Models/object.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

<a name="deleteDeviceConfiguration"></a>
# **deleteDeviceConfiguration**
> Object deleteDeviceConfiguration(deviceId, configurationId)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceId** | **String**|  | [default to null]
 **configurationId** | **String**|  | [default to null]

### Return type

[**Object**](../Models/object.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="getDeviceConfiguration"></a>
# **getDeviceConfiguration**
> Object getDeviceConfiguration(deviceId, configurationId)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceId** | **String**|  | [default to null]
 **configurationId** | **String**|  | [default to null]

### Return type

[**Object**](../Models/object.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="getDeviceConfigurations"></a>
# **getDeviceConfigurations**
> Object getDeviceConfigurations(deviceId)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceId** | **String**|  | [default to null]

### Return type

[**Object**](../Models/object.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="updateDeviceConfiguration"></a>
# **updateDeviceConfiguration**
> Object updateDeviceConfiguration(deviceId, configurationId, DeviceConfiguration)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceId** | **String**|  | [default to null]
 **configurationId** | **String**|  | [default to null]
 **DeviceConfiguration** | [**DeviceConfiguration**](../Models/DeviceConfiguration.md)|  |

### Return type

[**Object**](../Models/object.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

