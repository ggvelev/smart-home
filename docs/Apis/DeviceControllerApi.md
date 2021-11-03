# DeviceControllerApi

All URIs are relative to *https://localhost:8043*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createDevice**](DeviceControllerApi.md#createDevice) | **POST** /api/devices | 
[**getDevice**](DeviceControllerApi.md#getDevice) | **GET** /api/devices/{deviceId} | 
[**listDevices**](DeviceControllerApi.md#listDevices) | **GET** /api/devices | 
[**removeDevice**](DeviceControllerApi.md#removeDevice) | **DELETE** /api/devices/{deviceId} | 
[**updateDevice**](DeviceControllerApi.md#updateDevice) | **PUT** /api/devices/{deviceId} | 


<a name="createDevice"></a>
# **createDevice**
> DeviceDetails createDevice(DeviceRegistrationRequest)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **DeviceRegistrationRequest** | [**DeviceRegistrationRequest**](../Models/DeviceRegistrationRequest.md)|  |

### Return type

[**DeviceDetails**](../Models/DeviceDetails.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

<a name="getDevice"></a>
# **getDevice**
> DeviceDetails getDevice(deviceId)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceId** | **String**|  | [default to null]

### Return type

[**DeviceDetails**](../Models/DeviceDetails.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="listDevices"></a>
# **listDevices**
> List listDevices()



### Parameters
This endpoint does not need any parameter.

### Return type

[**List**](../Models/DeviceDetails.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="removeDevice"></a>
# **removeDevice**
> removeDevice(deviceId)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceId** | **String**|  | [default to null]

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="updateDevice"></a>
# **updateDevice**
> DeviceDetails updateDevice(deviceId, DeviceDetailsUpdateRequest)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceId** | **String**|  | [default to null]
 **DeviceDetailsUpdateRequest** | [**DeviceDetailsUpdateRequest**](../Models/DeviceDetailsUpdateRequest.md)|  |

### Return type

[**DeviceDetails**](../Models/DeviceDetails.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

