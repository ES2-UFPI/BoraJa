import { Text, View, StyleSheet } from 'react-native';
import MapView, { Marker, Region } from 'react-native-maps';
import { Button, Input } from 'react-native-elements';
import { useState, useEffect } from 'react';
import {
  requestForegroundPermissionsAsync,
  getCurrentPositionAsync,
  LocationObject
} from 'expo-location';

export default function DriverScreen() {
  const [location, setLocation] = useState<LocationObject | null>(null);
  const [region, setRegion] = useState<Region | null>(null);

  async function requestLocationPermissions() {
    const { granted } = await requestForegroundPermissionsAsync();
    if (granted) {
      const currentPosition = await getCurrentPositionAsync();
      setLocation(currentPosition);
      setRegion({
        latitude: currentPosition.coords.latitude,
        longitude: currentPosition.coords.longitude,
        latitudeDelta: 0.01, // Ajuste o valor para o nível de zoom desejado
        longitudeDelta: 0.01, // Ajuste o valor para o nível de zoom desejado
      });
    }
  }

  useEffect(() => {
    requestLocationPermissions();
  }, []);

  return (
    <View style={styles.container}>
      <View style={styles.mapContainer}>
        <MapView style={styles.map} region={region as Region}>
          {location && (
            <Marker
              coordinate={{
                latitude: location.coords.latitude,
                longitude: location.coords.longitude,
              }}
              title="Você está aqui"
            />
          )}
        </MapView>
      </View>
      <View style={styles.buttonContainer}>
        <Button title="Iniciar carona" buttonStyle={styles.buttonStyle2} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginTop: 200,
  },
  buttonContainer: {
    position: 'absolute',
    bottom: 50,
    zIndex: 1,
  },
  buttonStyle2: {
    height: 60,
    borderRadius: 12,
    width: 250,
    backgroundColor: '#F3AC3D',
  },
  mapContainer: {
    width: '100%',
    height: '100%',
    borderWidth: 2,
    borderColor: '#444',
  },
  map: {
    flex: 1,
  },
});