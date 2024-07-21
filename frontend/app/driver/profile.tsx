import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, Image } from 'react-native';
import { useRouter } from 'expo-router';
import { Button, Icon } from 'react-native-elements';
import { getTokenFromFile } from '../tokenFileStorage';
const { jwtDecode } = require('jwt-decode');

export default function Profile() {
  const router = useRouter();
  const [token, setToken] = useState<string | null>(null);
  const [driverData, setDriverData] = useState<any>(null);

  useEffect(() => {
    const fetchToken = async () => {
      const storedToken = await getTokenFromFile();
      setToken(storedToken);

      if (storedToken) {
        const decoded: any = jwtDecode(storedToken);
        const driverId = decoded.preferred_username;

        try {
          const response = await fetch(`http://localhost:8085/motorista/${driverId}`);
          const data = await response.json();
          setDriverData(data);
        } catch (error) {
          console.error('Erro ao buscar dados do motorista:', error);
        }
      }
    };
    fetchToken();
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Perfil</Text>
      <View style={styles.buttonSpacer} />
      <Image
        style={styles.profileImage}
        source={{ uri: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQMggZhOIH1vXmnv0bCyBu8iEuYQO-Dw1kpp7_v2mwhw_SKksetiK0e4VWUak3pm-v-Moc&usqp=CAU' }}
      />
      <Text style={styles.name}>Olá, {driverData ? `${driverData.nome}` : 'Carregando...'}</Text>
      <Text style={styles.stars}>{driverData ? `${driverData.avaliacao}` : 'Carregando...'}<Icon name="star" size={15} color="black"/></Text>
      <View style={styles.buttonContainer}>
        <Button title="Editar Perfil" onPress={() => router.push('screens/editProfile')} buttonStyle={styles.buttonStyle2} />
        <View style={styles.buttonSpacer} /> 
        <Button title="Ajuda" onPress={() => console.log(token)} buttonStyle={styles.buttonStyle2}/>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    padding: 20,
    paddingTop: 150, // Adicionei paddingTop para garantir que o título fique no topo
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 10,
    position: 'absolute', // Adicionei position absolute
    top: 30, // Ajuste a posição conforme necessário
  },
  profileImage: {
    width: 100,
    height: 100,
    borderRadius: 50,
    marginBottom: 20,
  },
  name: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  description: {
    fontSize: 16,
    color: '#666',
    textAlign: 'center',
  },
  stars: {
    fontSize: 16,
    color: '#666',
    textAlign: 'center',
  },
  buttonStyle2: {
    height: 60,
    backgroundColor: '#F3AC3D',
    borderRadius: 12,
  },

  buttonContainer: {
    width: '80%',
    justifyContent: 'center',
    marginTop: 100,
  },

  buttonSpacer: {
    height: 30,
  },
});
