import { Text, View, StyleSheet, Modal } from 'react-native';
import MapView, { Region } from 'react-native-maps';
import { Button, Input } from 'react-native-elements';
import { useRouter } from 'expo-router';
import { useState, useEffect } from 'react';
import { useLocalSearchParams } from 'expo-router';
import BackButton from '../../components/BackButton';
import {
  requestForegroundPermissionsAsync,
  getCurrentPositionAsync,
  LocationObject
} from 'expo-location';

export default function DriverScreen() {
  const router = useRouter();
  const [location, setLocation] = useState<LocationObject | null>(null);
  const [region, setRegion] = useState<Region | null>(null);
  const { token } = useLocalSearchParams();
  const [modalVisible, setModalVisible] = useState(false);
  const [confirmLocationVisible, setConfirmLocationVisible] = useState(false);
  const [tripDetails, setTripDetails] = useState({
    motoristaId: '',
    previsaoSaida: '',
    quantidadeVagas: 0,
    veiculoPlaca: '',
    localizacao: { latitude: 0, longitude: 0 },
  });

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

  const handleCreateTrip = async () => {
    try {
      const response = await fetch('http://26.78.193.223:8085/viagem', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(tripDetails),
      });
      const data = await response.json();
      setModalVisible(false);
      router.push({ pathname: 'screens/TripDetails', params: { tripId: data.id } });
    } catch (error) {
      console.error('Erro ao criar viagem:', error);
    }
  };

  const handleConfirmLocation = () => {
    if (region) {
      setTripDetails({
        ...tripDetails,
        localizacao: { latitude: region.latitude, longitude: region.longitude },
      });
      setConfirmLocationVisible(false);
    }
  };

  return (
    <View style={styles.container}>
      <BackButton/>
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
        <Button title="Iniciar carona" onPress={() => setModalVisible(true)} buttonStyle={styles.buttonStyle} />
      </View>
      <Modal visible={modalVisible} animationType="slide">
        <View style={styles.modalContainer}>
          <Text style={styles.modalTitle}>Iniciar Nova Viagem</Text>
          <Input placeholder="Horário de Saída" onChangeText={(text) => setTripDetails({ ...tripDetails, previsaoSaida: text })} />
          <Input placeholder="Quantidade de Vagas" keyboardType="numeric" onChangeText={(text) => setTripDetails({ ...tripDetails, quantidadeVagas: parseInt(text) })} />
          <Input placeholder="Veículo" onChangeText={(text) => setTripDetails({ ...tripDetails, veiculoPlaca: text })} />
          <View style={styles.buttonSpacer} />
          <View style={styles.rowContainer}>
            <Input
              placeholder="Latitude"
              keyboardType="numeric"
              value={tripDetails.localizacao.latitude.toString()}
              onChangeText={(text) => setTripDetails({ ...tripDetails, localizacao: { ...tripDetails.localizacao, latitude: parseFloat(text) } })}
              containerStyle={styles.halfInput}
            />
            <Input
              placeholder="Longitude"
              keyboardType="numeric"
              value={tripDetails.localizacao.longitude.toString()}
              onChangeText={(text) => setTripDetails({ ...tripDetails, localizacao: { ...tripDetails.localizacao, longitude: parseFloat(text) } })}
              containerStyle={styles.halfInput}
            />
            <Button title="⌖" onPress={() => setConfirmLocationVisible(true)} buttonStyle={styles.halfButton} titleStyle={styles.buttonTitle} />
          </View>
          <Button title="Criar Viagem" onPress={handleCreateTrip} buttonStyle={styles.buttonStyle} />
          <View style={styles.buttonSpacer} />
          <Button
            title="Cancelar"
            onPress={() => {
              setRegion({
                latitude: location?.coords.latitude ?? 0,
                longitude: location?.coords.longitude ?? 0,
                latitudeDelta: 0.01, // Ajuste conforme necessário
                longitudeDelta: 0.01, // Ajuste conforme necessário
              });
              setModalVisible(false);
            }}
            buttonStyle={styles.buttonStyle}
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
          <Button title="Cancelar" onPress={() => setConfirmLocationVisible(false)} buttonStyle={styles.buttonStyle} />
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
  },
});
