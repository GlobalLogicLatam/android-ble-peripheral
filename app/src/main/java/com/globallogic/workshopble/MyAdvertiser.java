package com.globallogic.workshopble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.ParcelUuid;
import android.widget.Toast;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Javier Bianciotto on 10/04/2017.
 * GlobalLogic | javier.bianciotto@globallogic.com
 */

public class MyAdvertiser {
    private static Logger logger = Logger.getLogger("MyAdvertiser");
    private static final String SERVICE_UUID = "163bb01e-f71c-48f4-a9de-2a0000aaaaaa";
    private static final String CHARACTERISTIC_UUID = "163bb01e-f71c-48f4-a9de-2a0000bbbbbb";
    private static final ParcelUuid ADVERTISEMENT_UUID = ParcelUuid.fromString(SERVICE_UUID);
    private static BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            logger.log(Level.INFO, "Connection state change");
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                logger.log(Level.INFO, device.getAddress() + " connected.");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                logger.log(Level.INFO, device.getAddress() + " disconnected.");
            }
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            logger.log(Level.INFO, "Received read in char: " + characteristic.getUuid());
            Long tsLong = System.currentTimeMillis()/1000;
            byte[] timestamp = tsLong.toString().getBytes();
            mGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, timestamp);
        }
    };

    private static AdvertiseCallback mAdvertiseCallback = null;
    private static BluetoothManager btManager;
    private static BluetoothGattServer mGattServer;


    public MyAdvertiser(Context context) {
        initGattServer(context);
    }


    private BluetoothManager getBluetoothManager(Context context) {
        if (btManager == null) {
            btManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        }
        return btManager;
    }

    private void initGattServer(Context context) {
        BluetoothManager manager = getBluetoothManager(context);
        if (manager != null) {
            mGattServer = manager.openGattServer(context, mGattServerCallback);
            BluetoothGattService service = new BluetoothGattService(UUID.fromString(SERVICE_UUID), BluetoothGattService.SERVICE_TYPE_PRIMARY);
            BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(UUID.fromString(CHARACTERISTIC_UUID), BluetoothGattCharacteristic.PROPERTY_READ, BluetoothGattCharacteristic.PERMISSION_READ);
            service.addCharacteristic(characteristic);
            mGattServer.addService(service);
        } else {
            logger.log(Level.INFO, "Bluetooth manager is null, can't init Gatt server");
        }


    }


    public void startAdvertising(Context context) {
        logger.log(Level.INFO, "startAdvertising");

        BluetoothLeAdvertiser btAdvertiser;
        BluetoothManager btManager = getBluetoothManager(context);
        if (btManager != null) {
            BluetoothAdapter btAdapter = btManager.getAdapter();
            if (btAdapter != null) {
                btAdvertiser = btAdapter.getBluetoothLeAdvertiser();
                if (btAdvertiser == null) {
                    logger.log(Level.INFO, "Bluetooth advertiser is null");
                    logger.log(Level.INFO, "Device does not support HFA");
                    return;
                }

                if (mAdvertiseCallback == null) {
                    setBLECallbacks();
                }

                btAdvertiser.startAdvertising(createAdvSettings(true, 0),
                        createFMPAdvertiseData(ADVERTISEMENT_UUID),
                        mAdvertiseCallback);

                logger.log(Level.INFO, "Advertising started");
                Toast.makeText(context, "Advertising started", Toast.LENGTH_SHORT).show();
            } else {
                logger.log(Level.INFO, "BluetoothAdapter is null");
            }
        } else {
            logger.log(Level.INFO, "BluetoothManager is null");
        }
    }

    private void setBLECallbacks() {
        mAdvertiseCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                logger.log(Level.INFO, "On start advertising success");
            }
        };
    }

    private AdvertiseData createFMPAdvertiseData(ParcelUuid uuid) {
        logger.log(Level.INFO, "Advertise UUID: " + uuid.toString());
        AdvertiseData.Builder builder = new AdvertiseData.Builder();

        builder.addServiceUuid(uuid);

        builder.setIncludeDeviceName(false);
        builder.setIncludeTxPowerLevel(false);

        return builder.build();
    }

    private static AdvertiseSettings createAdvSettings(boolean connectable, int timeoutMillis) {
        AdvertiseSettings.Builder builder = new AdvertiseSettings.Builder();
        builder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        builder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);

        builder.setConnectable(connectable);
        builder.setTimeout(timeoutMillis);
        return builder.build();
    }
}
