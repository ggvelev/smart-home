/*
  A demo sketch for ESP8266 NodeMCU device with DHT11 sensor.
  Device reports readings every 10 seconds and listens for incoming commands (only logging them to Serial output).
  When connectivity is established, the device sends its network config to devices/{id}/config to let smarthome-api server
  know it's address.
*/

#include "EspMQTTClient.h"
#include "DHT.h"

#define DHTTYPE    DHT11   // Select DHT sensor model: DHT 11


char* deviceUuid          = "caf703a4-44ab-4ac5-958b-0d1420167764";
char* mqttClientUsername  = "ENTER_YOURS";
char* mqttClientPassword  = "ENTER_YOURS!";
char* mqttBrokerAddress   = "ENTER_YOURS";
char* wifiSSID            = "ENTER_YOURS";
char* wifiPassword        = "ENTER_YOURS";

EspMQTTClient client(
  wifiSSID,
  wifiPassword,
  mqttBrokerAddress,
  mqttClientUsername,
  mqttClientPassword,
  deviceUuid,
  1883         // The MQTT broker port, default to 1883
);

// Initialize DHTPin with the pin number
// which DOUT (sensor's pin) is connected to:
uint8_t DHTPin = D8;
float hum = NAN, temp = NAN;

// Initialize DHT sensor object with pin and DHT model.
DHT dht(DHTPin, DHTTYPE);

void setup()
{
  Serial.begin(115200);

  client.enableDebuggingMessages();
  client.enableLastWillMessage("smarthome/lwt", "ESP8266_SENSOR going offline");
}

void onConnectionEstablished()
{
  Serial.printf("WiFi connected, IP address: %s\n", WiFi.localIP().toString().c_str());
  Serial.printf("MAC address = %s\n", WiFi.softAPmacAddress().c_str());
  Serial.println(getDeviceInfo());

  // Listen for commands and display them on Serial output (for the sake of the demo)
  client.subscribe("devices/caf703a4-44ab-4ac5-958b-0d1420167764/commands", [](const String & payload) {
    Serial.println(payload);
  });

  // Report device network configuration once
  client.executeDelayed(10 * 1000, []() {
    client.publish("devices/caf703a4-44ab-4ac5-958b-0d1420167764/config", getDeviceInfo(), true);
  });
}

String getDeviceInfo() {
  String info = "{";
  info += "\"ipAddress\": \"";
  info += WiFi.localIP().toString().c_str();
  info += "\",";
  info += "\"macAddress\": \"";
  info += WiFi.macAddress().c_str();
  info += "\",";
  info += "\"gatewayIpAddress\": \"";
  info += WiFi.gatewayIP().toString().c_str();
  info += "\",";
  info += "\"gatewaySubnetMask\": \"";
  info += WiFi.subnetMask().toString().c_str();
  info += "\",";
  info += "\"dnsIp\": \"";
  info += WiFi.dnsIP().toString().c_str();
  info += "\"}";
  return info;
}

String getTempReading() {
  String temperature = "{";
  temperature += "\"value\": \"";
  temperature += (float)temp;
  temperature += "\",";
  temperature += "\"type\": \"SENSOR_TEMPERATURE\",";
  temperature += "\"valueType\": \"FLOAT\",";
  temperature += "\"time\": null";
  temperature += "}";
  return temperature;
}

String getHumReading() {
  String humidity = "{";
  humidity += "\"value\": \"";
  humidity += (float)hum;
  humidity += "\",";
  humidity += "\"type\": \"SENSOR_HUMIDITY\",";
  humidity += "\"valueType\": \"FLOAT\",";
  humidity += "\"time\": null";
  humidity += "}";
  return humidity;
}

void loop()
{
  hum = dht.readHumidity(true);
  temp = dht.readTemperature();
  client.loop();

  // a little hack because the old DHT11 sensor is giving up and not working as well as it used to
  if (!isnan(hum) && hum <= 100 && hum >= 0) {
    client.publish("devices/caf703a4-44ab-4ac5-958b-0d1420167764/state", getHumReading());
  }

  if (!isnan(temp) && temp <= 100 && temp >= -50) {
    client.publish("devices/caf703a4-44ab-4ac5-958b-0d1420167764/state", getTempReading());
  }

  delay(10000);
}
