import React, { useState, useEffect } from 'react';
import { Text, View, StyleSheet, Modal } from 'react-native';
import { Button, Input } from 'react-native-elements';
import config from '../config';
import MapView, { Marker, Region } from 'react-native-maps';
import { useLocalSearchParams, useRouter } from 'expo-router';
import {
  requestForegroundPermissionsAsync,
  getCurrentPositionAsync,
  LocationObject
} from 'expo-location';
import Geocoder from 'react-native-geocoding';
import DateTimePickerModal from "react-native-modal-datetime-picker";
import BackButton from '@/components/BackButton';
const { jwtDecode } = require('jwt-decode');

Geocoder.init("AIzaSyD2015jWuZSr28R9O48DbSqb0wgxcLczUE");

export default function motoristaScreen() {
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
  const backendUrl = config.BACKEND_URL;
  const backendPort = config.PORT;

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
      // Atualizando os detalhes da viagem com a origem
      setTripDetails((prevDetails) => ({
        ...prevDetails,
        origem: {
          latitude: currentPosition.coords.latitude,
          longitude: currentPosition.coords.longitude,
          nome: 'Sua Localização', // Defina um nome fixo ou use Geocoder para obter um nome
        },
      }));
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
      const username = decodedToken.preferred_username;

      const formattedPrevisaoSaida = new Date(tripDetails.previsaoSaida).toISOString();
      const formattedPrevisaoChegada = new Date(tripDetails.previsaoChegada).toISOString();

      const response = await fetch(`http://${backendUrl}:${backendPort}/viagem`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          motoristaUsername: username,
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
      router.push({ pathname: 'screens/TripDetails', params: { tripId: data.data.id } });
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

  const handleConfirmLocation = async () => {
    if (region) {
      const response = await Geocoder.from(region.latitude, region.longitude);
      const address = response.results[0].formatted_address;

      setTripDetails({
        ...tripDetails,
        destino: { latitude: region.latitude, longitude: region.longitude, nome: address },
      });
      setConfirmLocationVisible(false);
    }
  };

  const handleAddressSubmit = async (address: string) => {
    try {
      const response = await Geocoder.from(address);
      const { lat, lng } = response.results[0].geometry.location;
      setRegion({
        latitude: lat,
        longitude: lng,
        latitudeDelta: 0.01,
        longitudeDelta: 0.01,
      });
      setTripDetails({ ...tripDetails, destino: { ...tripDetails.destino, nome: address, latitude: lat, longitude: lng } });
    } catch (error) {
      console.error("Erro ao buscar o endereço:", error);
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
      <BackButton />
      <View style={styles.mapContainer}>
        <MapView
          style={styles.map}
          region={region || undefined}
          scrollEnabled={true}
          zoomEnabled={true}
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
            placeholder="Placa do Veículo"
            onChangeText={(text) => setTripDetails({ ...tripDetails, veiculoPlaca: text })}
          />
          <View style={styles.buttonSpacer} />
          <View style={styles.rowContainer}>
            <Input
              placeholder="Endereço do Destino"
              value={tripDetails.destino.nome}
              onChangeText={(text) => setTripDetails({ ...tripDetails, destino: { ...tripDetails.destino, nome: text } })}
              onSubmitEditing={({ nativeEvent }) => handleAddressSubmit(nativeEvent.text)}
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
                latitudeDelta: 0.01,
                longitudeDelta: 0.01,
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
          <Button title="Confirmar Localização" onPress={handleConfirmLocation} buttonStyle={styles.buttonModal} />
          <Button title="Cancelar" buttonStyle={styles.buttonModal} onPress={() => setConfirmLocationVisible(false)} />
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
    bottom: 30,
    zIndex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
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
  buttonModal: {
    height: 50,
    borderRadius: 12,
    width: 180,
    backgroundColor: '#F3AC3D',
    marginHorizontal: 5,
    marginVertical: 5,
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
    marginTop: 20,
    maxHeight: 550,
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
    width: '80%',
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
