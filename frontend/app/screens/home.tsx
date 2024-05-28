import { Text, View, StyleSheet } from 'react-native';
import { Button } from 'react-native-elements';
import React, { useContext } from 'react';
import { AuthContext } from '../AuthProvider';
import { useRouter } from 'expo-router';
import { Image } from 'react-native';

export default function HomeScreen() {
  const { logout } = useContext(AuthContext);
  const router = useRouter();

  return (
    <View style={styles.container}>
      <View style={styles.titleContainer}>
        <Image source={require('@/assets/images/boraja_logo.jpg')} style={{ alignSelf: 'center', width: 250, height: 60 }} />
      </View>
      <View style={styles.buttonContainer}>
        <Button title="Entrar como motorista" onPress={() => router.push('driver/homeDriver')} buttonStyle={styles.buttonStyle1} />
        <View style={styles.buttonSpacer} /> 
        <Button title="Entrar como passageiro" onPress={() => router.push('passenger/homePassenger')} buttonStyle={styles.buttonStyle2} />
      </View>
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
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
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
    height: 30,
  },
});
