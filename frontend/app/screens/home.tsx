import { Text, View, StyleSheet } from 'react-native';
import { Button } from 'react-native-elements';
import React, { useContext } from 'react';
import { AuthContext } from '../AuthProvider'; // Importa o AuthContext

export default function HomeScreen() {
  const { logout } = useContext(AuthContext); // Usa o contexto de autenticação

  return (
    <View style={styles.container}>
      <View style={styles.titleContainer}>
        <Text style={styles.title}>Início</Text>
      </View>
      <View style={styles.buttonContainer}>
        <Button title="Entrar como motorista" onPress={() => console.log("Button 1 pressed")} buttonStyle={styles.buttonStyle1} />
        <View style={styles.buttonSpacer} /> 
        <Button title="Entrar como passageiro" onPress={() => console.log("Button 2 pressed")} buttonStyle={styles.buttonStyle2} />
        <Button title="Logout" onPress={logout} buttonStyle={styles.buttonStyle2} />
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
    marginTop: 50,
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
    height: 50,
    backgroundColor: 'black',
    borderRadius: 30,
  },
  buttonStyle2: {
    height: 50,
    backgroundColor: '#F3AC3D',
    borderRadius: 30,
  },
  buttonSpacer: {
    height: 30,
  },
});
