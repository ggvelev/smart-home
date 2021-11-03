# DevicePropertiesControllerApi

All URIs are relative to *https://localhost:8043*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addDeviceProperty**](DevicePropertiesControllerApi.md#addDeviceProperty) | **POST** /api/devices/{deviceId}/properties | 
[**deleteProperty**](DevicePropertiesControllerApi.md#deleteProperty) | **DELETE** /api/devices/{deviceId}/properties/{propertyId} | 
[**getDeviceProperty**](DevicePropertiesControllerApi.md#getDeviceProperty) | **GET** /api/devices/{deviceId}/properties/{propertyId} | 
[**listDeviceProperties**](DevicePropertiesControllerApi.md#listDeviceProperties) | **GET** /api/devices/{deviceId}/properties | 
[**listPropertyTypes**](DevicePropertiesControllerApi.md#listPropertyTypes) | **GET** /api/devices/property-types | 
[**updateDeviceProperty**](DevicePropertiesControllerApi.md#updateDeviceProperty) | **PUT** /api/devices/{deviceId}/properties/{propertyId} | 


<a name="addDeviceProperty"></a>
# **addDeviceProperty**
> DeviceProperty addDeviceProperty(deviceId, DeviceProperty)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceId** | **String**|  | [default to null]
 **DeviceProperty** | [**DeviceProperty**](../Models/DeviceProperty.md)|  |

### Return type

[**DeviceProperty**](../Models/DeviceProperty.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

<a name="deleteProperty"></a>
# **deleteProperty**
> deleteProperty(deviceId, propertyId)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceId** | **String**|  | [default to null]
 **propertyId** | **String**|  | [default to null]

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="getDeviceProperty"></a>
# **getDeviceProperty**
> DeviceProperty getDeviceProperty(deviceId, propertyId)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceId** | **String**|  | [default to null]
 **propertyId** | **String**|  | [default to null]

### Return type

[**DeviceProperty**](../Models/DeviceProperty.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="listDeviceProperties"></a>
# **listDeviceProperties**
> List listDeviceProperties(deviceId)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceId** | **String**|  | [default to null]

### Return type

[**List**](../Models/DeviceProperty.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="listPropertyTypes"></a>
# **listPropertyTypes**
> Map listPropertyTypes()



### Parameters
This endpoint does not need any parameter.

### Return type

[**Map**](../Models/set.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="updateDeviceProperty"></a>
# **updateDeviceProperty**
> DeviceProperty updateDeviceProperty(deviceId, propertyId, DeviceProperty)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceId** | **String**|  | [default to null]
 **propertyId** | **String**|  | [default to null]
 **DeviceProperty** | [**DeviceProperty**](../Models/DeviceProperty.md)|  |

### Return type

[**DeviceProperty**](../Models/DeviceProperty.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

