import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, Image, TextInput, TouchableOpacity } from 'react-native';
import { useRouter, useLocalSearchParams } from 'expo-router';
import { Button, Icon } from 'react-native-elements';
import { getTokenFromFile } from '../tokenFileStorage';
const { jwtDecode } = require('jwt-decode');
import * as ImagePicker from 'expo-image-picker';
import BackButton from '../../components/BackButton';

export default function Profile() {
  const router = useRouter();
  const [token, setToken] = useState<string | null>(null);
  const [driverData, setDriverData] = useState<any>(null);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [foto, setFoto] = useState('');

  useEffect(() => {
    const fetchToken = async () => {
      const storedToken = await getTokenFromFile();
      setToken(storedToken);

      if (storedToken) {
        const decoded: any = jwtDecode(storedToken);
        const driverId = decoded.preferred_username;

        try {
          const response = await fetch(`http://localhost:8085/motorista/${driverId}`);
          if (!response.ok) {
            throw new Error(`Erro ao buscar dados: ${response.statusText}`);
          }
          const data = await response.json();
          const { nome, email, foto, dataNascimento } = data.data;
          setDriverData(data.data);
          setName(nome);
          setEmail(email);
          setFoto(foto);
        } catch (error) {
          console.error('Erro ao buscar dados do motorista:', error);
        }
      }
    };
    fetchToken();
  }, []);

  const pickImage = async () => {
    let result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.All,
      allowsEditing: true,
      aspect: [4, 3],
      quality: 1,
    });

    console.log(result);

    if (!result.canceled) {
      setFoto(result.assets[0].uri);
    }
  };

  const handleUpdate = async () => {
    const driverId = driverData.id; // Assumindo que o ID do motorista está disponível em driverData.id
    try {
      const response = await fetch(`http://localhost:8085/motorista/${driverId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          nome: name,
          email: email,
          foto: foto,
        }),
      });

      if (response.ok) {
        console.log('Dados do motorista atualizados com sucesso!');
        // Você pode atualizar o estado driverData aqui se necessário
      } else {
        console.error('Erro ao atualizar dados do motorista:', response.status);
      }
    } catch (error) {
      console.error('Erro ao atualizar dados do motorista:', error);
    }
  };

  return (
    <View style={styles.container}>
      <BackButton />
      <Text style={styles.title}>Perfil</Text>
      <View style={styles.buttonSpacer} />
      <TouchableOpacity onPress={pickImage}>
        <Image
          style={styles.profileImage}
          source={{ uri: driverData ? driverData.foto : 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQMggZhOIH1vXmnv0bCyBu8iEuYQO-Dw1kpp7_v2mwhw_SKksetiK0e4VWUak3pm-v-Moc&usqp=CAU' }}
        />
      </TouchableOpacity>
      <Text style={styles.name}>Olá, {driverData ? `${driverData.nome}` : 'Carregando...'}</Text>
      <Text style={styles.stars}>{driverData ? `${driverData.avaliacao}` : 'Carregando...'} <Icon name="star" size={15} color="black"/></Text>
      <View style={styles.inputContainer}>
        <Text style={styles.label}>Nome:</Text>
        <TextInput
          style={styles.input}
          value={name}
          onChangeText={setName}
        />
        <Text style={styles.label}>Email:</Text>
        <TextInput
          style={styles.input}
          value={email}
          onChangeText={setEmail}
        />
        <Text style={styles.label}>Foto:</Text>
        <Button
          title="Escolher foto"
          onPress={pickImage}
          buttonStyle={styles.buttonStyle2}
        />
      </View>
      <View style={styles.buttonContainer}>
        <Button title="Editar Perfil" onPress={handleUpdate} buttonStyle={styles.buttonStyle2} />
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
  },
  buttonSpacer: {
    marginBottom: 10,
  },
  profileImage: {
    width: 100,
    height: 100,
    borderRadius: 50,
    marginBottom: 10,
  },
  name: {
    fontSize: 18,
    marginBottom: 5,
  },
  stars: {
    fontSize: 16,
    marginBottom: 10,
  },
  inputContainer: {
    width: '80%',
    marginBottom: 20,
  },
  label: {
    fontSize: 16,
    marginBottom: 5,
  },
  input: {
    borderWidth: 1,
    borderColor: '#ccc',
    padding: 10,
    borderRadius: 5,
    marginBottom: 10,
  },
  buttonContainer: {
    width: '80%',
    flexDirection: 'row',
    justifyContent: 'center',
  },
  buttonStyle2: {
    backgroundColor: '#007bff',
    padding: 10,
    borderRadius: 5,
    width: '100%',
    marginBottom: 10,
  },
});