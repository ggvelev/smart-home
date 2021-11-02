// WARNING: the following pieces of cluttered and unreadable (or hardly readable) code may cause
// headache, eye burn or bleeding! READ AT YOUR OWN RISK!

// Global variables for prop types, chart, JWT and user details, request URLs:
var PROPERTY_TYPES = [];
var chart;

var EMPTY_DEVICE = {
    deviceUuid: "", deviceMetadata: {
        locationBuilding: "",
        locationFloor: "",
        locationRoom: "",
        name: "",
        description: "",
        manufacturer: "",
        serialNumber: ""
    }, networkSettings: {
        macAddress: "",
        ipAddress: "",
        networkCidr: ""
    }
};

var JWT = null;
var USER = null;

const DEVICE_ID = "{deviceId}";
const USER_ID = "{userId}";
const PROPERTY_ID = "{propertyId}";

const BASE_URI = 'https://localhost:8043'
const API_BASE = BASE_URI + "/api";
const AUTHENTICATION = API_BASE + "/authentication";
const DEVICES = API_BASE + "/devices";
const DEVICE_BY_ID = DEVICES + "/{deviceId}";
const DEVICE_PROPERTY_TYPES = DEVICES + "/property-types";
const DEVICE_PROPERTIES = DEVICE_BY_ID + "/properties";
const DEVICE_PROPERTY_BY_ID = DEVICE_PROPERTIES + "/{propertyId}";
const DEVICE_COMMANDS = DEVICE_BY_ID + "/commands";
const DEVICE_STATE_HISTORY = DEVICE_PROPERTY_BY_ID + "/state-history";
const USERS = API_BASE + "/users";
const USER_BY_ID = USERS + "/{userId}";
const USER_NOTIFICATION_SETTINGS = USER_BY_ID + "/notification-settings";
const USER_DEVICE_NOTIFICATION_SETTINGS = USER_BY_ID + "/{deviceId}/notification-settings";


// ------------ Authentication

function authenticate(username, password) {
    $.POST(AUTHENTICATION, {username: username, password: password}).done(function (data) {
        JWT = data.token;
        USER = data.userDetails;
        $('#user-auth-register-container').html('');
        $('#smarthome-content-container').load('smarthome-content.html');
        $('#logout-btn').html('<button id="logout" style="margin: auto">Logout</button>');
        list_devices();
    }).fail(function (response) {
        var data = response.responseJSON;
        $('#user-login-messages').html(render_error_messages(response.status, data));
    });
}

function isAuthenticated() {
    return JWT != null;
}

function logout() {
    JWT = null;
    USER = null;
    $('#logout-btn').html('');
    $('#user-auth-register-container').load('user-auth-register.html');
    $('#smarthome-content-container').html('')
}

function getBearerTokenHeader() {
    return JWT != null ?
        {Authorization: 'Bearer ' + JWT} :
        null;
}

// ------------ Devices

function list_devices() {
    $.GET(DEVICES).done(function (data) {
        reload_property_types();
        $('#devices').html(render_devices(data));
        $('#devices-messages').html(render_info_messages('Successfully retrieved all devices!'));
    }).fail(function (response) {
        var data = response.responseJSON;
        $('#devices-messages').html(render_error_messages(data.message));
    });
}

// Update device details based on form input values:
function update_device_details(form) {
    var device = {
        deviceUuid: null,
        deviceMetadata: {
            locationBuilding: form.locationBuilding,
            locationFloor: form.locationFloor,
            locationRoom: form.locationRoom,
            name: form.name,
            description: form.description,
            manufacturer: form.manufacturer,
            serialNumber: form.serialNumber
        },
        networkSettings: {
            macAddress: form.macAddress,
            ipAddress: form.ipAddress,
            networkCidr: form.networkCidr
        }
    }

    $.PUT(DEVICE_BY_ID.replace(DEVICE_ID, form.id), device).done(function (data) {
        $('#device-add-edit-notifications').html('');
        $('#device-messages').html(render_info_messages('Device edited successfully!'));
        list_devices();
    }).fail(function (response) {
        var data = response.responseJSON;
        $('#device-messages').html(render_error_messages(response.status, data));
    });
}

// Create device based on form input values
function create_device(form) {
    var device = {
        deviceUuid: form.id,
        metadata: {
            locationBuilding: form.locationBuilding,
            locationFloor: form.locationFloor,
            locationRoom: form.locationRoom,
            name: form.name,
            description: form.description,
            manufacturer: form.manufacturer,
            serialNumber: form.serialNumber
        },
        networkSettings: {
            macAddress: form.macAddress,
            ipAddress: form.ipAddress,
            networkCidr: form.networkCidr
        }
    }

    $.POST(DEVICES, device).done(function (data) {
        $('#device-add-edit-notifications').html('');
        $('#device-messages').html(render_info_messages('Device added successfully!'));
        list_devices();
    }).fail(function (response) {
        var data = response.responseJSON;
        $('#device-messages').html(render_error_messages(response.status, data));
    });
}

function update_notification_settings(data) {
    if (data.emailDestination !== '') {
        var emailUpdate = {
            deviceId: data.deviceId,
            notificationSettings: [
                {
                    type: "EMAIL",
                    destination: data.emailDestination,
                    enabled: data.emailEnabled === 'on'
                }
            ]
        }

        $.PUT(USER_NOTIFICATION_SETTINGS.replace(USER_ID, USER.uuid), emailUpdate).done(function (data) {
            $('#device-add-edit-notifications').html('');
            $('#device-messages').html(render_info_messages('Notification settings updated!'));
            list_devices();
        }).fail(function (response) {
            var data = response.responseJSON;
            $('#device-messages').html(render_error_messages(response.status, data));
        });
    }

    if (data.slackDestination !== '') {
        var slackUpdate = {
            deviceId: data.deviceId,
            notificationSettings: [
                {
                    type: "SLACK",
                    destination: data.slackDestination,
                    enabled: data.slackEnabled === 'on'
                }
            ]
        }

        $.PUT(USER_NOTIFICATION_SETTINGS.replace(USER_ID, USER.uuid), slackUpdate).done(function (data) {
            $('#device-add-edit-notifications').html('');
            $('#device-messages').html(render_info_messages('Notification settings updated!'));
            list_devices();
        }).fail(function (response) {
            var data = response.responseJSON;
            $('#device-messages').html(render_error_messages(response.status, data));
        });
    }
}

function update_property_details(form) {
    var body = {
        propertyId: form.id,
        displayName: form.displayName,
        name: form.propType,
        type: form.propValueType,
        read: form.read === 'on',
        write: form.write === 'on'
    }

    $.PUT(DEVICE_PROPERTY_BY_ID.replace(DEVICE_ID, form.deviceId).replace(PROPERTY_ID, form.id), body).done(function (data) {
        $('#property-add-edit-execute').html('');
        $('#property-messages').html(render_info_messages('Property updated!'));
        list_devices();
        reload_properties(form.deviceName, form.deviceId);
    }).fail(function (response) {
        var data = response.responseJSON;
        $('#property-messages').html(render_error_messages(response.status, data));
    });
}

function create_property(form) {
    var body = {
        propertyId: null,
        displayName: form.displayName,
        name: form.propType,
        type: form.propValueType,
        read: form.read === 'on',
        write: form.write === 'on'
    }

    $.POST(DEVICE_PROPERTIES.replace(DEVICE_ID, form.deviceId), body).done(function (data) {
        $('#property-add-edit-execute').html('');
        $('#property-messages').html(render_info_messages('Property added!'));
        list_devices();
        reload_properties(form.deviceName, form.deviceId);
    }).fail(function (response) {
        var data = response.responseJSON;
        $('#property-messages').html(render_error_messages(response.status, data));
    });
}

function apply_property(form) {
    var body = {
        type: form.propName,
        value: form.cmdValue
    }

    $.POST(DEVICE_COMMANDS.replace(DEVICE_ID, form.deviceId), body).done(function (data) {
        $('#property-add-edit-execute').html('');
        $('#property-messages').html(render_info_messages('Command sent!'));
        list_devices();
        reload_properties(form.deviceName, form.deviceId);
    }).fail(function (response) {
        var data = response.responseJSON;
        $('#property-messages').html(render_error_messages(response.status, data));
    });
}

function reload_property_types() {
    $.GET(DEVICE_PROPERTY_TYPES).done(function (data) {
        PROPERTY_TYPES = data;
    }).fail(function (response) {
        var data = response.responseJSON;
        $('#devices-messages').html(render_error_messages(response.status, data));
    });
}

function reload_properties(deviceName, deviceId) {
    $.GET(DEVICE_PROPERTIES.replace(DEVICE_ID, deviceId)).done(function (data) {
        $('#properties').html(render_properties(deviceName, deviceId, data));
        $('#properties-messages').html(render_info_messages('Properties retrieved successfully!'));
    }).fail(function (response) {
        var data = response.responseJSON;
        $('#properties-messages').html(render_error_messages(response.status, data));
    });
}

$(document).ready(function () {

    if (!isAuthenticated()) {
        console.log('not authenticated')
        $('#user-auth-register-container').load('user-auth-register.html');
        $('#smarthome-content-container').html('');
    } else {
        list_devices();
        reload_property_types();
        console.log('authenticated')
        $('#user-auth-register-container').html('');
        $('#logout-btn').html('<button id="logout" style="margin: auto">Logout</button>');
        $('#smarthome-content-container').load('smarthome-content.html');
    }

    // ------------------ Auth/Register handlers -------------------
    $(document).on('submit', '#user-login > form', function () {
        var form = $(this).serializeObject();
        authenticate(form.username, form.password);
        return false;
    });

    $(document).on('submit', '#user-register > form', function () {
        var form = $(this).serializeObject();
        var body = {
            username: form.username,
            email: form.email,
            password: form.password
        }

        $.POST(USERS, body).done(function (data) {
            $('#user-register-messages').html(render_info_messages('Registered successfully'));
        }).fail(function (response) {
            var data = response.responseJSON;
            $('#user-register-messages').html(render_error_messages(data.message));
        });
        return false;
    });

    $(document).on('click', 'button#logout', function () {
        logout();
        return false;
    })

    // ------------------ DEVICES ------------------
    $(document).on('click', 'a.devices-refresh', function () {
        list_devices();
        return false;
    });

    $(document).on('click', 'a.device-add', function () {
        $('#device-add-edit-notifications').html(render_device_form(EMPTY_DEVICE));
        $('#device-messages').html('');
        return false;
    });

    $(document).on('click', 'a.device-edit', function () {
        var id = $(this).attr('data-device-id');
        $.GET(DEVICE_BY_ID.replace(DEVICE_ID, id)).done(function (data) {
            $('#device-add-edit-notifications').html(render_device_form(data));
        }).fail(function (response) {
            var data = response.responseJSON;
            $('#device-messages').html(render_error_messages(response.status, data));
        });
        return false;
    });

    $(document).on('click', 'a.device-notifications', function () {
        var deviceId = $(this).attr('data-device-id');
        $.GET(USER_DEVICE_NOTIFICATION_SETTINGS.replace(USER_ID, USER.uuid).replace(DEVICE_ID, deviceId)).done(function (data) {
            $('#device-add-edit-notifications').html(render_notification_settings(data));
        }).fail(function (response) {
            var data = response.responseJSON;
            $('#device-messages').html(render_error_messages(response.status, data));
        });
        return false;
    });

    $(document).on('click', 'a.device-delete', function () {
        var id = $(this).attr('data-device-id');
        $.DELETE(DEVICE_BY_ID.replace(DEVICE_ID, id)).done(function (data) {
            list_devices();
            $('#device-messages').html(render_info_messages('Device deleted successfully!'));
        }).fail(function (response) {
            var data = response.responseJSON;
            $('#device-messages').html(render_error_messages(response.status, data));
        });
        return false;
    });

    // Device/Notifications form cancel button handler
    $(document).on('click', 'button#device-form-cancel', function () {
        $('#device-add-edit-notifications').html('');
        $('#device-messages').html('');
        return false;
    });

    // --------------- device/notifications form submission handling
    $(document).on('submit', '#device-add-edit-notifications > form', function () {
        var form = $(this).serializeObject();

        if (form.formType === 'device') {
            // Handle device add/update request
            if (form.op === 'update') {
                update_device_details(form);
            } else {
                create_device(form);
            }
        } else {
            // Handle notifications update request
            update_notification_settings(form);
        }
        return false;
    });

    // ------------------ PROPERTIES ------------------
    $(document).on('click', 'a.device-properties, a.properties-refresh', function () {
        var device_id = $(this).attr('data-device-id');
        var device_name = $(this).attr('data-device-name');
        reload_properties(device_name, device_id);
        $('#property-add-edit-execute').html('');
        $('#property-messages').html('');
        return false;
    });


    $(document).on('click', 'button#device-property-form-cancel', function () {
        $('#property-add-edit-execute').html('');
        $('#property-messages').html('');
        return false;
    });

    $(document).on('click', 'a.prop-edit', function () {
        var device_id = $(this).attr('data-device-id');
        var device_name = $(this).attr('data-device-name');
        var prop_id = $(this).attr('data-prop-id');

        $.GET(DEVICE_PROPERTY_BY_ID.replace(DEVICE_ID, device_id).replace(PROPERTY_ID, prop_id)).done(function (data) {
            $('#property-add-edit-execute').html(render_property_form(device_name, device_id, data));
        }).fail(function (response) {
            $('#property-messages').html(render_error_messages(response.status, response.responseJSON))
        });

        return false;
    });

    $(document).on('click', 'a.prop-add', function () {
        var device_id = $(this).attr('data-device-id');
        var device_name = $(this).attr('data-device-name');
        var prop = {
            propertyId: "",
            displayName: "",
            name: "",
            type: "",
            read: "",
            write: ""
        }
        $('#property-add-edit-execute').html(render_property_form(device_name, device_id, prop));
        return false;
    });

    $(document).on('click', 'a.prop-delete', function () {
        var device_id = $(this).attr('data-device-id');
        var prop_id = $(this).attr('data-prop-id');
        var device_name = $(this).attr('data-device-name');

        $.DELETE(DEVICE_PROPERTY_BY_ID.replace(DEVICE_ID, device_id).replace(PROPERTY_ID, prop_id)).done(function (data) {
            reload_properties(device_name, device_id);
        }).fail(function (response) {
            var data = response.responseJSON;
            $('#properties-messages').html(render_error_messages(response.status, data));
        });

        return false;
    });

    // Draw chart on property click
    $(document).on('click', 'a.property, a.prop-history', function () {
        var device_id = $(this).attr('data-device-id');
        var prop_id = $(this).attr('data-prop-id');
        var prop_name = $(this).attr('data-prop-name');


        $.GET(DEVICE_STATE_HISTORY.replace(DEVICE_ID, device_id).replace(PROPERTY_ID, prop_id)).done(function (data) {
            if (!data || data.length < 1) {
                if (chart !== undefined) {
                    chart.destroy();
                }
                $('#property-chart-diagram').html('');
                $('#chart-message').html("<p class='info_icon'>No records available.</p>");
                $('#property-history-table').html("<p class='info_icon'>No records available.</p>");
                return;
            }
            $('#property-chart-diagram').html('')
                .append('<canvas id="chart-canvas"></canvas><br>')
                .append($('<button id="chart-close">Close</button>'))
                .append($("<button id='delete-records' data-device-id='" + device_id + "' data-prop-id='" + prop_id + "'>DELETE RECORDS</button>"));
            render_chart(prop_name, data);// draw chart with the retrieved data
            $('#property-history-table').html(render_history_table(prop_name, data));
        }).fail(function (response) {
            var data = response.responseJSON;
            $('#properties-messages').html(render_error_messages(response.status, data));
        });

        return false;
    });

    $(document).on('click', 'button#delete-records', function () {
        var device_id = $(this).attr('data-device-id');
        var prop_id = $(this).attr('data-prop-id');

        $.DELETE(DEVICE_STATE_HISTORY.replace(DEVICE_ID, device_id).replace(PROPERTY_ID, prop_id)).done(function (data) {
            if (chart !== undefined) {
                chart.destroy();
            }
            $('#property-chart-diagram').html('');
            $('#chart-message').html("<p class='info_icon'>Records were deleted!</p>");
            $('#property-history-table').html("<p class='info_icon'>Records were deleted!</p>");
        }).fail(function (response) {
            var data = response.responseJSON;
            $('#chart-message').html(render_error_messages(response.status, data));
        });
        return false;
    });

    $(document).on('click', 'button#chart-close', function () {
        if (chart !== undefined) {
            chart.destroy();
        }
        $('#property-chart-diagram').html('');
        return false;
    });

    $(document).on('click', 'button#device-property-history-cancel', function () {
        $('#property-history-table').html('<p class="info_icon">Select a device property to view its historical changes.</p>');
        return false;
    });


    $(document).on('submit', '#property-add-edit-execute > form', function () {
        var form = $(this).serializeObject();

        if (form.formType === 'prop') {
            // Handle property add/update request
            if (form.id !== undefined) {
                update_property_details(form);
            } else {
                create_property(form);
            }
        } else {
            // Handle sending/applying value for a property (i.e. sending device command) request
            apply_property(form);
        }
        reload_properties(form.deviceName, form.deviceId);
        return false;
    })

    $(document).on('click', 'a.prop-apply', function () {
        var prop_id = $(this).attr('data-prop-id');
        var device_id = $(this).attr('data-device-id');
        var device_name = $(this).attr('data-device-name');

        $.GET(DEVICE_PROPERTY_BY_ID.replace(DEVICE_ID, device_id).replace(PROPERTY_ID, prop_id)).done(function (data) {
            $('#property-add-edit-execute').html(render_property_apply_form(device_name, device_id, data));
        }).fail(function (response) {
            var data = response.responseJSON;
            $('#properties-messages').html(render_error_messages(response.status, data));
        });

        return false;
    });
});
