# DeviceTelemetryControllerApi

All URIs are relative to *https://localhost:8043*

Method | HTTP request | Description
------------- | ------------- | -------------
[**deleteRecords**](DeviceTelemetryControllerApi.md#deleteRecords) | **DELETE** /api/devices/{deviceId}/properties/{propertyId}/state-history | 
[**fetchStateHistory**](DeviceTelemetryControllerApi.md#fetchStateHistory) | **GET** /api/devices/{deviceId}/properties/{propertyId}/state-history | 


<a name="deleteRecords"></a>
# **deleteRecords**
> deleteRecords(deviceId, propertyId)



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

<a name="fetchStateHistory"></a>
# **fetchStateHistory**
> List fetchStateHistory(deviceId, propertyId, forLastHours)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceId** | **String**|  | [default to null]
 **propertyId** | **String**|  | [default to null]
 **forLastHours** | **Long**|  | [optional] [default to 24]

### Return type

[**List**](../Models/DeviceState.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

