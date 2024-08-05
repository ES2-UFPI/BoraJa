import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, Image } from 'react-native';
import { useRouter } from 'expo-router';
import { Button, Icon } from 'react-native-elements';
import { getTokenFromFile } from '../tokenFileStorage';
const { jwtDecode } = require('jwt-decode');
import config from '../config';

// Carregar as variáveis de ambiente do arquivo .env
export default function Profile() {
  const router = useRouter();
  const [token, setToken] = useState<string | null>(null);
  const [passengerData, setPassengerData] = useState<any>(null);
  const backendUrl = config.BACKEND_URL;
  const backendPort = config.PORT;

  useEffect(() => {
    const fetchToken = async () => {
      const storedToken = await getTokenFromFile();
      setToken(storedToken);

      if (storedToken) {
        const decoded: any = jwtDecode(storedToken);
        const passengerId = decoded.preferred_username;

        try {
          const response = await fetch(`http://${backendUrl}:${backendPort}/passageiro/${passengerId}`);
          const data = await response.json();
          setPassengerData(data.data);
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
      <Text style={styles.name}>Olá, {passengerData ? `${passengerData.nome}` : 'Carregando...'}</Text>
      <Text style={styles.stars}>{passengerData ? `${passengerData.avaliacao}` : 'Carregando...'}<Icon name="star" size={15} color="black"/></Text>
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
    paddingTop: 150,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 10,
    position: 'absolute',
    top: 30,
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
