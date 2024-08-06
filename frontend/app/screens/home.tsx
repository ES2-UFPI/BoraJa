import React, { useState, useEffect } from 'react';
import { Text, View, StyleSheet, Modal, TextInput, TouchableOpacity, Image, Animated, Dimensions } from 'react-native';
import { Button } from 'react-native-elements';
import { useRouter } from 'expo-router';
import { useLocalSearchParams } from 'expo-router';
import * as ImagePicker from 'expo-image-picker';
import DateTimePicker from '@react-native-community/datetimepicker';
const { jwtDecode } = require('jwt-decode');
import config from '../config';

type UserType = 'motorista' | 'passageiro';

const { width } = Dimensions.get('window');

interface JwtPayload {
  preferred_username: string;
}

export default function HomeScreen() {
  const router = useRouter();
  const { token } = useLocalSearchParams();
  const [isModalVisible, setModalVisible] = useState(false);
  const [userType, setUserType] = useState<UserType | null>(null);
  const [formData, setFormData] = useState({
    foto: '',
    cpf: '',
    nome: '',
    email: '',
    dataNascimento: '2024-08-06',
    username: ''
  });
  const [vehicleData, setVehicleData] = useState({
    placa: '',
    foto: '',
    marca: '',
    modelo: '',
    cor: '',
    ano: '',
    tipo: 'CARRO',
    cpfProprietario: '',
  });
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [showVehicleForm, setShowVehicleForm] = useState(false);
  const [fadeAnim] = useState(new Animated.Value(1));
  const backendUrl = config.BACKEND_URL;
  const backendPort = config.PORT;

  useEffect(() => {
    if (token) {
      const decoded: JwtPayload = jwtDecode(token);
      setFormData(prevFormData => ({
        ...prevFormData,
        username: decoded.preferred_username
      }));
    }
  }, [token]);

  const checkUserExists = async (type: UserType) => {
    const url = `http://${backendUrl}:${backendPort}/${type}/${formData.username}`;
    try {
      const response = await fetch(url);
      if (response.ok) {
        const exists = await response.json();
        return exists;
      }
      return false;
    } catch (error) {
      console.error(error);
      return false;
    }
  };

  const handlePress = async (type: UserType) => {
    const userExists = await checkUserExists(type);
    if (userExists) {
      router.push({ pathname: `${type}/home${type.charAt(0).toUpperCase() + type.slice(1)}`, params: { token: token } });
    } else {
      setUserType(type);
      setModalVisible(true);
    }
  };

  const handleImagePick = async (isVehicle = false) => {
    let result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.Images,
      allowsEditing: true,
      aspect: [4, 3],
      quality: 1,
    });

    if (!result.canceled) {
      if (isVehicle) {
        setVehicleData({ ...vehicleData, foto: result.assets[0].uri });
      } else {
        setFormData({ ...formData, foto: result.assets[0].uri });
      }
    }
  };

  const handleDateChange = (event: any, selectedDate: Date | undefined) => {
    const currentDate = selectedDate || new Date(formData.dataNascimento);
    setShowDatePicker(false);
    setFormData({ ...formData, dataNascimento: currentDate.toISOString().split('T')[0] });
  };

  const handleSubmit = async () => {
    if (!userType) return;
    const url = `http://${backendUrl}:${backendPort}/${userType}`;
    try {
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });
       //if (response.ok) {
        if (userType === 'motorista') {
          Animated.timing(fadeAnim, {
            toValue: 0,
            duration: 500,
            useNativeDriver: true,
          }).start(() => {
            setShowVehicleForm(true);
            Animated.timing(fadeAnim, {
              toValue: 1,
              duration: 500,
              useNativeDriver: true,
            }).start();
          });
        } else {
          setModalVisible(false);
          router.push({ pathname: `${userType}/home${userType.charAt(0).toUpperCase() + userType.slice(1)}`, params: { token: token } });
        }
      //}
    } catch (error) {
      console.error(error);
    }
  };

  const handleVehicleSubmit = async () => {
    const url = `http://${backendUrl}:${backendPort}/vehicle`;
    try {
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(vehicleData),
      });
      if (response.ok) {
        setModalVisible(false);
        router.push({ pathname: `motorista/homeMotorista`, params: { token: token } });
      }
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.titleContainer}>
        <Image source={require('@/assets/images/boraja_logo.jpg')} style={{ alignSelf: 'center', width: 250, height: 60 }} />
      </View>
      <View style={styles.buttonContainer}>
        <Button title="Entrar como motorista" onPress={() => handlePress('motorista')} buttonStyle={styles.buttonStyle1} />
        <View style={styles.buttonSpacer} />
        <Button title="Entrar como passageiro" onPress={() => handlePress('passageiro')} buttonStyle={styles.buttonStyle2} />
      </View>

      <Modal visible={isModalVisible} transparent={true}>
        <View style={styles.modalBackground}>
          <Animated.View style={[styles.modalContainer, { opacity: fadeAnim }]}>
            {!showVehicleForm ? (
              <>
                <Text>Cadastro {userType}</Text>
                <TextInput
                  placeholder="CPF"
                  value={formData.cpf}
                  onChangeText={(text) => setFormData({ ...formData, cpf: text })}
                  style={styles.input}
                />
                <TextInput
                  placeholder="Nome"
                  value={formData.nome}
                  onChangeText={(text) => setFormData({ ...formData, nome: text })}
                  style={styles.input}
                />
                <TextInput
                  placeholder="Email"
                  value={formData.email}
                  onChangeText={(text) => setFormData({ ...formData, email: text })}
                  style={styles.input}
                />
                <TouchableOpacity onPress={() => setShowDatePicker(true)} style={styles.input}>
                  <Text>Data de Nascimento: {formData.dataNascimento}</Text>
                </TouchableOpacity>
                {showDatePicker && (
                  <DateTimePicker
                  value={new Date(formData.dataNascimento)}
                  mode="date"
                  display="default"
                  onChange={handleDateChange}
                  />
                )}
                <Button style={styles.buttonStyle1} title="Escolher Foto" onPress={() => handleImagePick(true)}>Escolher Foto</Button>
                {vehicleData.foto ? <Image source={{ uri: vehicleData.foto }} style={styles.image} /> : null}
                <View style={styles.buttonSpacer} />
                {formData.foto ? <Image source={{ uri: formData.foto }} style={styles.image} /> : null}
                <Button title="Cadastrar" onPress={handleSubmit} />
              </>
            ) : (
              <>
                <Text>Cadastro de Veículo</Text>
                <TextInput
                  placeholder="Placa"
                  value={vehicleData.placa}
                  onChangeText={(text) => setVehicleData({ ...vehicleData, placa: text })}
                  style={styles.input}
                />
                <TextInput
                  placeholder="Marca"
                  value={vehicleData.marca}
                  onChangeText={(text) => setVehicleData({ ...vehicleData, marca: text })}
                  style={styles.input}
                />
                <TextInput
                  placeholder="Modelo"
                  value={vehicleData.modelo}
                  onChangeText={(text) => setVehicleData({ ...vehicleData, modelo: text })}
                  style={styles.input}
                />
                <TextInput
                  placeholder="Cor"
                  value={vehicleData.cor}
                  onChangeText={(text) => setVehicleData({ ...vehicleData, cor: text })}
                  style={styles.input}
                />
                <TextInput
                  placeholder="Ano"
                  value={vehicleData.ano}
                  onChangeText={(text) => setVehicleData({ ...vehicleData, ano: text })}
                  style={styles.input}
                />
                <TextInput
                  placeholder="CPF do Proprietário"
                  value={vehicleData.cpfProprietario}
                  onChangeText={(text) => setVehicleData({ ...vehicleData, cpfProprietario: text })}
                  style={styles.input}
                />
                <Button style={styles.buttonStyle1} title="Escolher Foto" onPress={() => handleImagePick(true)}>Escolher Foto</Button>
                {vehicleData.foto ? <Image source={{ uri: vehicleData.foto }} style={styles.image} /> : null}
                <View style={styles.buttonSpacer} />
                <Button title="Cadastrar Veículo" onPress={handleVehicleSubmit} />
              </>
            )}
          </Animated.View>
        </View>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white',
  },
  titleContainer: {
    position: 'absolute',
    top: 0,
    width: '100%',
    alignItems: 'center',
    marginTop: 200,
  },
  buttonContainer: {
    width: '80%',
    justifyContent: 'center',
    marginTop: 100,
  },
  buttonStyle1: {
    height: 60,
    backgroundColor: 'black',
    borderRadius: 12,
  },
  buttonStyle2: {
    height: 60,
    backgroundColor: '#F3AC3D',
    borderRadius: 12,
  },
  buttonSpacer: {
    height: 10,
  },
  buttonSpacer2: {
    height: 30,
  },
  modalBackground: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },
  modalContainer: {
    width: width * 0.8,
    padding: 20,
    backgroundColor: 'white',
    borderRadius: 10,
  },
  input: {
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    marginBottom: 10,
    padding: 10,
    justifyContent: 'center',
  },
  imagePicker: {
    alignItems: 'center',
    marginBottom: 10,
  },
  image: {
    width: 100,
    height: 100,
    marginBottom: 10,
  },
});