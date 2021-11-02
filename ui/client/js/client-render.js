// All these function render piece of HTML to plug into the DOM tree.
// The HTML can be plugged using $('#id').html(new_html);

// WARNING: the following pieces of cluttered and unreadable (or hardly readable) code may cause
// headache, eye burn or bleeding! READ AT YOUR OWN RISK!

function render_devices(devices) {
    var html = "<tr>" +
        "<th>Name</th>" +
        "<th>Description</th>" +
        "<th>Location</th>" +
        "<th>S/N</th>" +
        "<th>Manufacturer</th>" +
        "<th>Network settings</th>" +
        "<th>Action</th>" +
        "</tr>";

    for (var i = 0; i < devices.length; i++) {
        var p = devices[i];
        html += "<tr>";
        html += "<td><a href='#' data-device-id='" + p.deviceId + "' data-device-name='" + p.deviceMetadata.name + "' class='device_icon device-properties'>" + html_escape(p.deviceMetadata.name) + "</a></td>";
        html += "<td>" + p.deviceMetadata.description + "</td>";
        html += "<td>" + p.deviceMetadata.locationBuilding + ', ' + p.deviceMetadata.locationFloor + ', ' + p.deviceMetadata.locationRoom + "</td>";
        html += "<td>" + p.deviceMetadata.serialNumber + "</td>";
        html += "<td>" + p.deviceMetadata.manufacturer + "</td>";
        html += "<td>" + '[' + p.networkSettings.macAddress + '] ' + p.networkSettings.ipAddress + "</td>";
        html += "<td>";
        html += "<a href='#' data-device-id='" + p.deviceId + "' class='edit_icon device-edit'>Edit</a><br>";
        html += "<a href='#' data-device-id='" + p.deviceId + "' class='delete_icon device-delete'>Delete</a><br>";
        html += "<a href='#' data-device-id='" + p.deviceId + "' class='notification_icon device-notifications'>Notifications</a><br>";
        html += "</td>";
        html += "</tr>";
    }

    html = "<table class='grid'>" + html + "</table>";
    return html;
}

function render_device_form(device) {
    if (!device) return 'Empty device.';

    var isNew = (!device.deviceId);

    var html = '';
    var title = isNew ? 'Add device' : 'Edit device';

    html += "<h1>" + title + "</h1>";
    html += "<form action='#' method='post'>";
    html += "<input name='formType' value='device' hidden/>"
    html += "<input name='op' value='" + (isNew ? "add" : "update") + "' hidden/>"
    html += "<p><label>ID</label><input name='id' value='" + (!isNew ? (html_escape(device.deviceId) + "' readonly='readonly'") : ("")) + "'/></p>";
    html += "<p><label>Name</label><input name='name' value='" + html_escape(device.deviceMetadata.name) + "'/></p>";

    html += "<p><label>Description</label><input name='description' value='" + html_escape(device.deviceMetadata.description) + "'/></p>";
    html += "<p><label>Building</label><input name='locationBuilding' value='" + html_escape(device.deviceMetadata.locationBuilding) + "'/></p>";
    html += "<p><label>Floor</label><input name='locationFloor' value='" + html_escape(device.deviceMetadata.locationFloor) + "'/></p>";
    html += "<p><label>Room</label><input name='locationRoom' value='" + html_escape(device.deviceMetadata.locationRoom) + "'/></p>";
    html += "<p><label>S/N</label><input name='serialNumber' value='" + (!isNew ? (html_escape(device.deviceMetadata.serialNumber) + "' readonly='readonly'") : ("")) + "'/></p>";
    html += "<p><label>Manufacturer</label><input name='manufacturer' value='" + html_escape(device.deviceMetadata.manufacturer) + "'/></p>";
    html += "<p><label>MAC address</label><input name='macAddress' value='" + html_escape(device.networkSettings.macAddress) + "'/></p>";
    html += "<p><label>IP address</label><input name='ipAddress' value='" + html_escape(device.networkSettings.ipAddress) + "'/></p>";
    html += "<p><label>Network CIDR</label><input name='networkCidr' value='" + html_escape(device.networkSettings.networkCidr) + "'/></p>";

    // html += "<p><label>Type</label>";
    // html += "<select name='deviceType' class='txt medium'>";
    // html += "<option value=''> </option>";
    // for (var i = 0; i < PROPERTY_TYPES.length; i++) {
    //     var device_type = PROPERTY_TYPES[i];
    //     var selected = (device.deviceType === device_type) ? 'selected' : '';
    //     html += "<option value='" + device_type + "' " + selected + ">" + device_type + "</option>";
    // }
    // html += "</select>";
    // html += "</p>";

    html += "<p><button>Save</button></p>";
    html += "<p><button id='device-form-cancel'>Cancel</button></p>";
    html += "</form>";

    return html;
}

function render_notification_settings(settings) {
    var html = "<h1>Notification preferences</h1>";

    var slack = null;
    var email = null;

    for (i = 0; i < settings.notificationSettings.length; i++) {
        if (settings.notificationSettings[i].type === 'SLACK') slack = settings.notificationSettings[i];
        if (settings.notificationSettings[i].type === 'EMAIL') email = settings.notificationSettings[i];
    }

    html += "<form action='#' method='post'>";
    html += "<input name='formType' value='notifications' hidden/>";
    html += "<input name='deviceId' value='" + settings.deviceId + "' hidden/>";
    html += "<p>";
    html += "<label>Slack</label>";
    html += "<input name='slackDestination' value='";
    if (slack !== null) html += slack.destination;
    html += "'/>";
    html += "<input name='slackEnabled' type='checkbox'";
    if (slack !== null) html += slack.enabled ? " checked" : "";
    html += " />"
    html += "</p>";
    html += "<p>";
    html += "<label>Email</label>"
    html += "<input name='emailDestination' value='";
    if (email !== null) html += email.destination;
    html += "'/>";
    html += "<input name='emailEnabled' type='checkbox'";
    if (email !== null) html += email.enabled ? " checked" : "";
    html += " />"

    html += "</p>"
    html += "<p><button>Save</button></p>"
    html += "<p><button id='device-form-cancel'>Cancel</button></p>"
    html += "</form>"

    return html;
}

// ---------------------------------------------------------

// TODO - reuse for device state history?
function render_sys_messages(data, id) {

    if ($('#sys-messages table').length > 0) {

        // If the given row is not already created, then create it:
        if ($('tr#row' + id).val() === undefined) {
            var row = $('<tr></tr>');
            row.attr('id', 'row' + id);
            row.append($('<td></td>').html(data.topicDescription));
            row.append($('<td></td>').html(data.payload));
            $('#sys-messages table').append(row);
        } else {
            var theRow = $('tr#row' + id);
            theRow.children('td').eq(0).html(data.topicDescription);
            theRow.children('td').eq(1).html(data.payload);
        }
    } else {
        var table = $('<table></table>').addClass('grid');
        table.attr('id', 'sys-topics')

        // Create table's headers and append to table:
        var headersRow = $('<tr></tr>');
        headersRow.append($('<th></th>').html('Metric description'));
        headersRow.append($('<th></th>').html('Value'));
        table.append(headersRow);

        // Create and fill table's row with data:
        var rowData = $('<tr></tr>');
        rowData.attr('id', 'row' + id);
        rowData.append($('<td></td>').html(data.topicDescription));
        rowData.append($('<td></td>').html(data.payload));
        table.append(rowData);

        $('#sys-messages').append(table);
    }

}

/*
If you ever, ever, read this whole mess called "front-end", I hope your eyes are not bleeding.
Sorry for the whole pain these pieces of code might cause. As a back-end guy I barely understand
how to adequately create good and structured web UI (and I feel a bit of shame)  :) GG & HF!
 */

// Render commands panel
function render_properties(deviceName, deviceId, props) {
    var html = '';

    html += "<p class='device_icon'>";
    html += "<b>" + html_escape(deviceName) + "</b> - ";
    html += (props.length > 0) ? props.length + ' properties available' : 'no properties available for this device.';
    html += "</p>";

    if (props.length > 0) {
        html += "<table class='grid'>";
        html += "<tr>";
        html += "<th>Description</th>";
        html += "<th>Name</th>";
        html += "<th>Type</th>";
        html += "<th>Read</th>";
        html += "<th>Write</th>";
        html += "<th>Action</th>";
        html += "</tr>";

        for (var i = 0; i < props.length; i++) {
            var p = props[i];
            html += "<tr>";
            html += "<td><a href='#' data-prop-id='" + p.propertyId + "' data-prop-name='" + p.displayName + "' data-device-id='" + deviceId + "' class='app_icon property'>" + html_escape(p.displayName) + "</a></td>";
            html += "<td>" + html_escape(p.name) + "</td>"
            html += "<td>" + html_escape(p.type) + "</td>"
            html += "<td>" + html_escape(p.read) + "</td>"
            html += "<td>" + html_escape(p.write) + "</td>"
            html += "<td>";
            html += "<a href='#' data-prop-id='" + p.propertyId + "' data-prop-name='" + p.name + "' data-device-name='" + deviceName + "' data-device-id='" + deviceId + "' class='edit_icon prop-edit'>Edit</a> ";
            html += "<a href='#' data-prop-id='" + p.propertyId + "' data-prop-name='" + p.name + "' data-device-name='" + deviceName + "' data-device-id='" + deviceId + "' class='delete_icon prop-delete'>Delete</a> ";
            html += "<a href='#' data-prop-id='" + p.propertyId + "' data-prop-name='" + p.name + "' data-device-name='" + deviceName + "' data-device-id='" + deviceId + "' class='history_icon prop-history'>History</a>";

            if (p.write)
                html += "<a href='#' data-prop-id='" + p.propertyId + "' data-device-id='" + deviceId + "' data-device-name='" + deviceName + "' class='execute_icon prop-apply'>Execute</a> ";

            html += "</td>";
            html += "</tr>";
        }
        html += "</table>";
    }

    html += "<p>" +
        "<a href='#' data-device-id='" + deviceId + "' data-device-name='" + deviceName + "' class='add_icon prop-add'>Add new property</a> " +
        "<a href='#' data-device-id='" + deviceId + "' data-device-name='" + deviceName + "' class='refresh_icon properties-refresh'>Refresh</a>" +
        "</p>";

    return html;
}

function render_property_form(device_name, deviceId, prop) {
    if (!prop) return 'Empty device property.';

    var isNew = (!prop.propertyId);

    var html = '';
    var title = isNew ? 'Add property' : 'Edit property';

    html += "<h1>" + title + "</h1>";
    html += "<form action='#' method='post'>";
    html += "<input type='hidden' name='deviceId' value='" + deviceId + "'>";
    html += "<input type='hidden' name='deviceName' value='" + device_name + "'>";
    html += "<input type='hidden' name='formType' value='prop'>";

    if (!isNew) {
        html += "<p><label>ID</label><input name='id' value='" + html_escape(prop.propertyId) + "' readonly='readonly'" + "'/></p>";
    }

    html += "<p><label>Display name</label><input name='displayName' value='" + (!isNew ? prop.displayName : "") + "' /></p>";
    html += "<p><label>Type</label>";
    html += "<select name='propType' class='txt medium'>";

    if (!isNew) {
        html += "<option value='" + prop.name + "'>" + prop.name + "</option>";
    } else {
        html += "<option value=''> </option>";
    }

    for (let i = 0; i < PROPERTY_TYPES.propertyTypes.length; i++) {
        html += "<option value='" + PROPERTY_TYPES.propertyTypes[i] + "'" + ">" + PROPERTY_TYPES.propertyTypes[i] + "</option>";
    }
    html += "</select>";
    html += "</p>";
    html += "<p><label>Value type</label>";
    html += "<select name='propValueType' class='txt medium'>";

    if (!isNew) {
        html += "<option value='" + prop.type + "'>" + prop.type + "</option>";
    } else {
        html += "<option value=''> </option>";
    }

    for (let i = 0; i < PROPERTY_TYPES.valueTypes.length; i++) {
        html += "<option value='" + PROPERTY_TYPES.valueTypes[i] + "'" + ">" + PROPERTY_TYPES.valueTypes[i] + "</option>";
    }
    html += "</select>";
    html += "</p>";

    html += "<p><label>Read</label>";
    html += "<input name='read' type='checkbox'";
    if (prop.propertyId) html += prop.read ? " checked" : "";
    html += " />"

    html += "<p><label>Write</label>";
    html += "<input name='write' type='checkbox'";
    if (prop.propertyId) html += prop.write ? " checked" : "";
    html += " />"

    html += "<p><button>Save</button></p>";
    html += "<p><button id='device-property-form-cancel'>Cancel</button></p>"
    html += "</form>";

    return html;
}

function render_property_apply_form(device_name, deviceId, prop) {
    var html = '';
    var title = "Send <b>" + prop.name + "</b> command for device <b>" + device_name + "</b>.";

    html += "<h1>" + title + "</h1>";
    html += "<form action='#' method='post'>";
    html += "<input type='hidden' name='deviceId' value='" + deviceId + "'>";
    html += "<input type='hidden' name='propName' value='" + prop.name + "'>";
    html += "<input type='hidden' name='deviceName' value='" + device_name + "'>";
    html += "<input type='hidden' name='formType' value='cmd'>";
    html += "<p><label>Value (" + prop.type + ")</label><input name='cmdValue'/></p>";
    html += "<p><button>Send</button></p>";
    html += "<p><button id='device-property-form-cancel'>Cancel</button></p>"
    html += "</form>";

    return html;
}

function render_chart(prop_name, data) {

    if (!data || data.length < 1)
        return;

    if (chart != null) {
        chart.destroy();
    }

    var lbls = [];
    var ds = [];
    for (let i = 0; i < data.length; i++) {
        lbls[i] = data[i].time;
        ds[i] = data[i].value;
    }

    var cfgData = {
        labels: lbls,
        datasets: [{
            label: prop_name,
            backgroundColor: 'rgb(255, 99, 132)',
            borderColor: 'rgb(255, 99, 132)',
            data: ds
        }]
    }

    var config = {
        type: 'line',
        data: cfgData,
        options: {}
    }

    chart = new Chart($('canvas#chart-canvas'), config);
}


function render_history_table(prop_name, data) {
    var html = '';

    html += "<b>" + html_escape(prop_name) + "</b> - ";
    html += (data.length > 0) ? data.length + ' historical records available' : 'no historical records available for this device property.';
    html += "</p>";

    if (data.length > 0) {
        html += "<div style='overflow-y: auto; height: 512px;'>";
        html += "<table id='history' class='grid'>";
        html += "<tr>";
        html += "<th>Value</th>";
        html += "<th>Name</th>";
        html += "<th>Type</th>";
        html += "<th>Time</th>";
        html += "</tr>";

        html += "<tbody class='overflow-y: auto; overflow-x: hidden; height: 100px;'>";
        for (var i = data.length-1 ; i >= 0 ; i--) {
            var p = data[i];
            html += "<tr>";
            html += "<td>" + html_escape(p.value) + "</td>"
            html += "<td>" + html_escape(p.type) + "</td>"
            html += "<td>" + html_escape(p.valueType) + "</td>"
            html += "<td>" + html_escape(p.time) + "</td>"
            html += "</tr>";
        }
        html += "</table>";
        html += "</div>";
    }

    html += "<p><button id='device-property-history-cancel'>Cancel</button></p>"

    return html;
}

function render_mqtt_publish_form(data) {
    var html = '';

    html += "<form id='submit-publish-form' action='#' method='post'>";
    html += "<p><label>MQTT topic</label><input name='topic' value='" + html_escape(data.topic) + "'/></p>";
    html += "<p><label>Payload</label><input name='payload' value='" + html_escape(data.payload) + "'/></p>";
    html += "<p><button id='submit-publish'>Publish</button></p>";
    html += "</form>";

    return html;
}

function render_mqtt_subscribe_form(topic) {
    var html = '';

    html += "<form id='submit-subscribe-form' action='#' method='post'>";
    html += "<p><label>MQTT topic</label><input name='topic' value='" + html_escape(topic) + "'/></p>";
    html += "<p><button id='submit-subscribe'>Subscribe</button></p>";
    html += "<p><button id='submit-unsubscribe' value='" + topic + "'>Unsubscribe</button></p>";
    html += "</form>";

    return html;
}

function render_error_messages(status, msg) {
    var html = '';
    if (msg) {
        var css = (msg) ? 'error_icon' : 'info_icon';
        html += "<p class='" + css + "'>" + "[" + status + "]" + msg.error + ' - ' + msg.message + ".</p>";
    }
    return html;
}

function render_info_messages(message) {
    var html = '';
    if (message) {
        var css = 'info_icon';
        html += "<p class='" + css + "'>" + message + "</p>";
    }
    return html;
}

function html_escape(val) {
    return (val + '')
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/\"/g, '&quot;')
        .replace(/\'/g, '&apos;');
}
