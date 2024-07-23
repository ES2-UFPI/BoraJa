import React, { useState, useEffect } from 'react';
import { Text, View, StyleSheet, Modal } from 'react-native';
import { Button } from 'react-native-elements';
import MapView, { Marker, Region } from 'react-native-maps';
import { useLocalSearchParams, useRouter } from 'expo-router';
import {
  requestForegroundPermissionsAsync,
  getCurrentPositionAsync,
  LocationObject
} from 'expo-location';
import { Input } from 'react-native-elements';
import DateTimePickerModal from "react-native-modal-datetime-picker";
const { jwtDecode } = require('jwt-decode');

export default function DriverScreen() {
  const router = useRouter();
  const [location, setLocation] = useState<LocationObject | null>(null);
  const [region, setRegion] = useState<Region | null>(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [confirmLocationVisible, setConfirmLocationVisible] = useState(false);
  const [isDatePickerVisible, setDatePickerVisibility] = useState(false);
  const [tripDetails, setTripDetails] = useState({
    motoristaId: '',
    previsaoSaida: '',
    previsaoChegada: '',
    quantidadeVagas: 0,
    veiculoPlaca: '',
    origem: { latitude: 0, longitude: 0, nome: '' },
    destino: { latitude: 0, longitude: 0, nome: '' },
  });
  const { token } = useLocalSearchParams();

  const showDatePicker = () => {
    setDatePickerVisibility(true);
  };

  const hideDatePicker = () => {
    setDatePickerVisibility(false);
  };

  async function requestLocationPermissions() {
    const { granted } = await requestForegroundPermissionsAsync();
    if (granted) {
      const currentPosition = await getCurrentPositionAsync();
      setLocation(currentPosition);
      setRegion({
        latitude: currentPosition.coords.latitude,
        longitude: currentPosition.coords.longitude,
        latitudeDelta: 0.01, // Adjusted for desired zoom level
        longitudeDelta: 0.01, // Adjusted for desired zoom level
      });
    }
  }

  useEffect(() => {
    requestLocationPermissions();
  }, []);

  const calculateEstimatedArrivalTime = (distance: number) => {
    const estimatedTimeInMinutes = distance / 50 * 60; // Assuming an average speed of 50 km/h
    const estimatedArrivalTime = new Date(tripDetails.previsaoSaida);
    estimatedArrivalTime.setMinutes(estimatedArrivalTime.getMinutes() + estimatedTimeInMinutes);
    return estimatedArrivalTime.toISOString();
  };

  const handleCreateTrip = async () => {
    try {
      const decodedToken = jwtDecode(token);
      const motoristaId = decodedToken.preferred_username;

      const formattedPrevisaoSaida = new Date(tripDetails.previsaoSaida).toISOString();
      const formattedPrevisaoChegada = new Date(tripDetails.previsaoChegada).toISOString();

      console.log(motoristaId, formattedPrevisaoSaida, formattedPrevisaoChegada, tripDetails.quantidadeVagas, tripDetails.veiculoPlaca, tripDetails.origem, tripDetails.destino);

      const response = await fetch('http://localhost:8085/viagem', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          motoristaId,
          previsaoSaida: formattedPrevisaoSaida,
          previsaoChegada: formattedPrevisaoChegada,
          quantidadeVagas: tripDetails.quantidadeVagas,
          veiculoPlaca: tripDetails.veiculoPlaca,
          origem: tripDetails.origem,
          destino: tripDetails.destino,
        }),
      });
      const data = await response.json();
      setModalVisible(false);
      router.push({ pathname: 'screens/TripDetails', params: { tripId: data.id } });
    } catch (error) {
      console.error('Erro ao criar viagem:', error);
    }
  };

  const handleConfirm = (date: Date) => {
    const updatedTripDetails = { ...tripDetails, previsaoSaida: date.toISOString() };
    const distance = calculateDistance(
      updatedTripDetails.origem.latitude,
      updatedTripDetails.origem.longitude,
      updatedTripDetails.destino.latitude,
      updatedTripDetails.destino.longitude
    );
    const previsaoChegada = new Date(date.getTime() + 10 * 60000);
    updatedTripDetails.previsaoChegada = previsaoChegada.toISOString();
    setTripDetails(updatedTripDetails);
    hideDatePicker();
  };

  const handleConfirmLocation = () => {
    if (region) {
      setTripDetails({
        ...tripDetails,
        destino: { latitude: region.latitude, longitude: region.longitude, nome: "" },
      });
      setConfirmLocationVisible(false);
    }
  };

  const calculateDistance = (lat1: number, lon1: number, lat2: number, lon2: number): number => {
    const R = 6371; // Radius of the earth in km
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a =
      0.5 - Math.cos(dLat) / 2 +
      Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * 
      (1 - Math.cos(dLon)) / 2;
    return R * 2 * Math.asin(Math.sqrt(a));
  };

  return (
    <View style={styles.container}>
      <View style={styles.mapContainer}>
        <MapView
          style={styles.map}
          region={region || undefined}
          scrollEnabled={false}
          zoomEnabled={false}
          rotateEnabled={false}
          pitchEnabled={false}
        />
        {location && (
          <View style={styles.locationMarkerContainer}>
            <View style={styles.miraOuter}>
              <View style={styles.miraInner} />
            </View>
            <Text style={styles.locationText}>Sua localização atual</Text>
          </View>
        )}
      </View>
      <View style={styles.buttonContainer}>
        <Button title="Iniciar carona" buttonStyle={styles.buttonStyle} onPress={() => setModalVisible(true)} />
      </View>
      <Modal visible={modalVisible} animationType="slide">
        <View style={styles.modalContainer}>
          <Text style={styles.modalTitle}>Iniciar Nova Viagem</Text>
          <Button title="Selecionar Horário de Saída" buttonStyle={styles.buttonHour} onPress={showDatePicker} />
          <View style={styles.buttonSpacer} />
          <Input
            placeholder="Quantidade de Vagas"
            keyboardType="numeric"
            onChangeText={(text) => setTripDetails({ ...tripDetails, quantidadeVagas: parseInt(text) })}
          />
          <Input
            placeholder="Veículo"
            onChangeText={(text) => setTripDetails({ ...tripDetails, veiculoPlaca: text })}
          />
          <View style={styles.buttonSpacer} />
          <View style={styles.rowContainer}>
            <Input
              placeholder="Latitude"
              keyboardType="numeric"
              value={tripDetails.destino.latitude.toString()}
              editable={false}
              containerStyle={styles.halfInput}
            />
            <Input
              placeholder="Longitude"
              keyboardType="numeric"
              value={tripDetails.destino.longitude.toString()}
              editable={false}
              containerStyle={styles.halfInput}
            />
          <Button
              title="⌖"
              onPress={() => setConfirmLocationVisible(true)}
              buttonStyle={styles.halfButton}
              titleStyle={styles.buttonTitle}
            />
          </View>
          <Button title="Criar Viagem" buttonStyle={styles.buttonStyle} onPress={handleCreateTrip} />
          <View style={styles.buttonSpacer} />
          <Button
            title="Cancelar"
            buttonStyle={styles.buttonStyle}
            onPress={() => {
              setRegion({
                latitude: location?.coords.latitude ?? 0,
                longitude: location?.coords.longitude ?? 0,
                latitudeDelta: 0.01, // Ajusted as necessary
                longitudeDelta: 0.01, // Ajusted as necessary
              });
              setModalVisible(false);
            }}
          />
          <DateTimePickerModal
            isVisible={isDatePickerVisible}
            mode="datetime"
            onConfirm={handleConfirm}
            onCancel={hideDatePicker}
          />
        </View>
      </Modal>
      <Modal visible={confirmLocationVisible} animationType="slide">
        <View style={styles.modalContainer}>
          <Text style={styles.modalTitle}>Confirme a Localização</Text>
          <View style={styles.mapContainer}>
            <MapView
              style={styles.map}
              region={region || undefined}
              onRegionChangeComplete={(reg) => setRegion(reg)}
            />
            <View style={styles.pointerContainer}>
              <View style={styles.miraOuter}>
                <View style={styles.miraInner} />
              </View>
            </View>
          </View>
          <Button title="Confirmar Localização" onPress={handleConfirmLocation} buttonStyle={styles.buttonStyle} />
          <Button title="Cancelar" buttonStyle={styles.buttonStyle} onPress={() => setConfirmLocationVisible(false)} />
        </View>
      </Modal>
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
  buttonContainer: {
    position: 'absolute',
    bottom: 50,
    zIndex: 1,
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: '90%',
    paddingHorizontal: 10,
  },
  buttonStyle: {
    height: 50,
    borderRadius: 12,
    width: 180,
    backgroundColor: '#F3AC3D',
    marginHorizontal: 5,
  },
  buttonHour: {
    height: 50,
    borderRadius: 12,
    width: 280,
    backgroundColor: '#F3AC3D',
    marginHorizontal: 5,
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
  buttonTitle: {
    fontSize: 50,  // Ajuste o tamanho do símbolo conforme necessário
    color: '#fff',
    marginTop: -20,
  },
  pointerContainer: {
    position: 'absolute',
    top: '50%',
    left: '50%',
    marginLeft: -15,
    marginTop: -15,
    zIndex: 10,
  },
  pointer: {
    width: 30,
    height: 30,
    backgroundColor: 'red',
    borderRadius: 15,
    borderWidth: 2,
    borderColor: '#fff',
  },
  locationMarkerContainer: {
    position: 'absolute',
    top: '50%',
    left: '50%',
    alignItems: 'center',
    justifyContent: 'center',
    marginLeft: -63,
    marginTop: -15,
  },
  locationMarker: {
    width: 30,
    height: 30,
    backgroundColor: 'blue',
    borderRadius: 15,
    borderWidth: 2,
    borderColor: '#fff',
  },
  locationText: {
    marginTop: 5,
    fontSize: 14,
    fontWeight: 'bold',
    color: '#000',
    textAlign: 'center',
  },
  modalContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  modalTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  buttonSpacer: {
    height: 10,
  },
  rowContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: '100%',
  },
  halfInput: {
    width: '40%',
  },
  halfButton: {
    height: 50,
    borderRadius: 12,
    backgroundColor: '#F3AC3D',
    marginHorizontal: 5,
    width: '47%',
  },
  miraOuter: {
    width: 30,
    height: 30,
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 15,
    borderWidth: 2,
    borderColor: 'red',
    backgroundColor: 'transparent',
  },
  miraInner: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: 'red',
  }
});
