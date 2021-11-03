# UserControllerApi

All URIs are relative to *https://localhost:8043*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getDeviceNotificationSettings**](UserControllerApi.md#getDeviceNotificationSettings) | **GET** /api/users/{userId}/{deviceId}/notification-settings | 
[**getNotificationSettings**](UserControllerApi.md#getNotificationSettings) | **GET** /api/users/{userId}/notification-settings | 
[**getUser**](UserControllerApi.md#getUser) | **GET** /api/users/{userId} | 
[**listUsers**](UserControllerApi.md#listUsers) | **GET** /api/users | 
[**recoverUserAccount**](UserControllerApi.md#recoverUserAccount) | **POST** /api/users/recovery | 
[**registerUser**](UserControllerApi.md#registerUser) | **POST** /api/users | 
[**removeUser**](UserControllerApi.md#removeUser) | **DELETE** /api/users/{userId} | 
[**updateNotificationSettings**](UserControllerApi.md#updateNotificationSettings) | **PUT** /api/users/{userId}/notification-settings | 
[**updateUserDetails**](UserControllerApi.md#updateUserDetails) | **PUT** /api/users/{userId} | 


<a name="getDeviceNotificationSettings"></a>
# **getDeviceNotificationSettings**
> UserNotificationSettings getDeviceNotificationSettings(userId, deviceId)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | **String**|  | [default to null]
 **deviceId** | **String**|  | [default to null]

### Return type

[**UserNotificationSettings**](../Models/UserNotificationSettings.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="getNotificationSettings"></a>
# **getNotificationSettings**
> List getNotificationSettings(userId)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | **String**|  | [default to null]

### Return type

[**List**](../Models/UserNotificationSettings.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="getUser"></a>
# **getUser**
> UserDetails getUser(userId)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | **String**|  | [default to null]

### Return type

[**UserDetails**](../Models/UserDetails.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="listUsers"></a>
# **listUsers**
> List listUsers()



### Parameters
This endpoint does not need any parameter.

### Return type

[**List**](../Models/UserDetails.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="recoverUserAccount"></a>
# **recoverUserAccount**
> Object recoverUserAccount(action, recoveryId, userEmail, newPassword)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **action** | **String**|  | [default to null] [enum: init, finish]
 **recoveryId** | **String**|  | [optional] [default to null]
 **userEmail** | **String**|  | [optional] [default to null]
 **newPassword** | **String**|  | [optional] [default to null]

### Return type

[**Object**](../Models/object.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="registerUser"></a>
# **registerUser**
> UserDetails registerUser(CreateUserRequest)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **CreateUserRequest** | [**CreateUserRequest**](../Models/CreateUserRequest.md)|  |

### Return type

[**UserDetails**](../Models/UserDetails.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

<a name="removeUser"></a>
# **removeUser**
> removeUser(userId, softDelete)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | **String**|  | [default to null]
 **softDelete** | **Boolean**|  | [optional] [default to false]

### Return type

null (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: */*

<a name="updateNotificationSettings"></a>
# **updateNotificationSettings**
> UserNotificationSettings updateNotificationSettings(userId, UserNotificationSettings)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | **String**|  | [default to null]
 **UserNotificationSettings** | [**UserNotificationSettings**](../Models/UserNotificationSettings.md)|  |

### Return type

[**UserNotificationSettings**](../Models/UserNotificationSettings.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

<a name="updateUserDetails"></a>
# **updateUserDetails**
> Object updateUserDetails(userId, body)



### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | **String**|  | [default to null]
 **body** | **Object**|  |

### Return type

[**Object**](../Models/object.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: */*

