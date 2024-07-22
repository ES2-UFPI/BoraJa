import { Text, View, StyleSheet, Modal, FlatList, Image, TouchableOpacity } from 'react-native';
import MapView, { Marker, Region } from 'react-native-maps';
import { useState, useEffect } from 'react';
import { useLocalSearchParams } from 'expo-router';
import {
  requestForegroundPermissionsAsync,
  getCurrentPositionAsync,
  LocationObject
} from 'expo-location';
import { useNavigation } from '@react-navigation/native';

export default function PassengerScreen() {
  const [location, setLocation] = useState<LocationObject | null>(null);
  const [region, setRegion] = useState<Region | null>(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [trips, setTrips] = useState([]);
  const { token } = useLocalSearchParams();
  const navigation = useNavigation();

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

  const handleSearchTrips = async () => {
    try {
      const response = await fetch(`{{url}}/viagem/search`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const data = await response.json();
      setTrips(data);
      setModalVisible(true);
    } catch (error) {
      console.error('Erro ao buscar viagens:', error);
    }
  };

  const renderTripItem = ({ item }: { item: any }) => (
    <View style={styles.tripItem}>
      <Image source={require('../../assets/images/car.png')} style={styles.carIcon} />
      <View style={styles.tripDetails}>
        <Text style={styles.tripTitle}>Motorista: {item.motoristaNome}</Text>
        <Text style={styles.tripInfo}>Veículo: {item.veiculoPlaca}</Text>
        <Text style={styles.tripInfo}>Partida: {item.origem.nome}</Text>
        <Text style={styles.tripInfo}>Chegada: {item.destino.nome}</Text>
        <Text style={styles.tripInfo}>Horário de Saída: {item.previsaoSaida}</Text>
        <Text style={styles.tripInfo}>Vagas Disponíveis: {item.quantidadeVagas}</Text>
      </View>
    </View>
  );

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
        <TouchableOpacity style={styles.buttonStyle2} onPress={handleSearchTrips}>
          <Text style={styles.buttonText}>Buscar Carona</Text>
        </TouchableOpacity>
      </View>
      <Modal visible={modalVisible} animationType="slide">
        <View style={styles.modalContainer}>
          <Text style={styles.modalTitle}>Viagens Disponíveis</Text>
          <FlatList
            data={trips}
            renderItem={renderTripItem}
            keyExtractor={(item) => item.id.toString()}
          />
          <TouchableOpacity style={styles.buttonStyle2} onPress={() => setModalVisible(false)}>
            <Text style={styles.buttonText}>Fechar</Text>
          </TouchableOpacity>
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
  mapContainer: {
    width: '100%',
    height: '100%',
    borderWidth: 2,
    borderColor: '#444',
  },
  map: {
    flex: 1,
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
    alignItems: 'center',
    justifyContent: 'center',
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
  modalContainer: {
    flex: 1,
    padding: 20,
  },
  modalTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  tripItem: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 10,
    marginBottom: 10,
    backgroundColor: '#f2f2f2',
    borderRadius: 8,
  },
  carIcon: {
    width: 40,
    height: 40,
    marginRight: 10,
  },
  tripDetails: {
    flex: 1,
  },
  tripTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  tripInfo: {
    fontSize: 14,
    marginBottom: 2,
  },
});
