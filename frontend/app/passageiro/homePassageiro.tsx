import React, { useState, useEffect } from 'react';
import { Text, View, StyleSheet, Modal, FlatList, Image, TouchableOpacity } from 'react-native';
import MapView, { Marker, Region } from 'react-native-maps';
import { requestForegroundPermissionsAsync, getCurrentPositionAsync, LocationObject } from 'expo-location';
import { useLocalSearchParams } from 'expo-router';
import { useNavigation } from '@react-navigation/native';
import config from '../config';

const useLocation = () => {
  const [location, setLocation] = useState<LocationObject | null>(null);
  const [region, setRegion] = useState<Region | undefined>(undefined);

  useEffect(() => {
    const requestLocationPermissions = async () => {
      const { granted } = await requestForegroundPermissionsAsync();
      if (granted) {
        const currentPosition = await getCurrentPositionAsync();
        setLocation(currentPosition);
        setRegion({
          latitude: currentPosition.coords.latitude,
          longitude: currentPosition.coords.longitude,
          latitudeDelta: 0.01,
          longitudeDelta: 0.01,
        });
      }
    };

    requestLocationPermissions();
  }, []);

  return { location, region };
};

const TripItem = ({ item }: { item: any }) => (
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

export default function PassageiroScreen() {
  const { location, region } = useLocation();
  const [modalVisible, setModalVisible] = useState(false);
  const [trips, setTrips] = useState([]);
  const { token } = useLocalSearchParams();
  const backendUrl = `${config.BACKEND_URL}:${config.PORT}`;

  const handleSearchTrips = async () => {
    try {
      const response = await fetch(`${backendUrl}/viagem/search`, {
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

  return (
    <View style={styles.container}>
      <Map region={region} location={location} />
      <View style={styles.buttonContainer}>
        <TouchableOpacity style={styles.buttonStyle} onPress={handleSearchTrips}>
          <Text style={styles.buttonText}>Buscar Carona</Text>
        </TouchableOpacity>
      </View>
      <TripsModal
        visible={modalVisible}
        trips={trips}
        onClose={() => setModalVisible(false)}
      />
    </View>
  );
}

const Map = ({ region, location }: { region: Region | undefined; location: LocationObject | null }) => (
  <View style={styles.mapContainer}>
    <MapView style={styles.map} region={region}>
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
);

const TripsModal = ({ visible, trips, onClose }: { visible: boolean; trips: any[]; onClose: () => void }) => (
  <Modal visible={visible} animationType="slide">
    <View style={styles.modalContainer}>
      <Text style={styles.modalTitle}>Viagens Disponíveis</Text>
      <FlatList
        data={trips}
        renderItem={({ item }) => <TripItem item={item} />}
        keyExtractor={(item) => item.id.toString()}
      />
      <TouchableOpacity style={styles.buttonStyle} onPress={onClose}>
        <Text style={styles.buttonText}>Fechar</Text>
      </TouchableOpacity>
    </View>
  </Modal>
);

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
  buttonStyle: {
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
