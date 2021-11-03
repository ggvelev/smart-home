# AuthenticationControllerApi

All URIs are relative to *https://localhost:8043*

Method | HTTP request | Description
------------- | ------------- | -------------
[**authenticate**](AuthenticationControllerApi.md#authenticate) | **POST** /api/authentication | 
[**get**](AuthenticationControllerApi.md#get) | **GET** /api/authentication | 


<a name="authenticate"></a>
# **authenticate**
> JwtAuthentication authenticate(UsernamePasswordAuthenticationRequest)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **UsernamePasswordAuthenticationRequest** | [**UsernamePasswordAuthenticationRequest**](../Models/UsernamePasswordAuthenticationRequest.md)|  |

### Return type

[**JwtAuthentication**](../Models/JwtAuthentication.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

<a name="get"></a>
# **get**
> UserDetails get()



### Parameters
This endpoint does not need any parameter.

### Return type

[**UserDetails**](../Models/UserDetails.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

