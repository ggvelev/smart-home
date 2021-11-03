# DeviceCommandControllerApi

All URIs are relative to *https://localhost:8043*

Method | HTTP request | Description
------------- | ------------- | -------------
[**deleteCommandHistory**](DeviceCommandControllerApi.md#deleteCommandHistory) | **DELETE** /api/devices/{deviceId}/commands/history | 
[**fetchCommandHistory**](DeviceCommandControllerApi.md#fetchCommandHistory) | **GET** /api/devices/{deviceId}/commands/history | 
[**issueCommand**](DeviceCommandControllerApi.md#issueCommand) | **POST** /api/devices/{deviceId}/commands | 


<a name="deleteCommandHistory"></a>
# **deleteCommandHistory**
> deleteCommandHistory(deviceId)



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

<a name="fetchCommandHistory"></a>
# **fetchCommandHistory**
> Object fetchCommandHistory(deviceId)



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

<a name="issueCommand"></a>
# **issueCommand**
> issueCommand(deviceId, DeviceCommand)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceId** | **String**|  | [default to null]
 **DeviceCommand** | [**DeviceCommand**](../Models/DeviceCommand.md)|  |

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

