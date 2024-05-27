import { Text, View, StyleSheet } from 'react-native';
import { Button, Input } from 'react-native-elements';
import { Image } from 'react-native'; // Import the Image component from react-native

export default function PassengerScreen() {
  return (

    <View style={styles.container}>
      <View style={styles.titleContainer}>
        <Text style={styles.title}>Passageiro</Text>
      </View>

      <View style={styles.buttonContainer}>
        <Input placeholder='Nome Completo' />
        <Input placeholder='CPF' />
        <Input placeholder='Data de Nascimento' />
        <Input placeholder='Foto' />
        <Button title="Salvar" onPress={() => console.log("Salvar")} buttonStyle={styles.buttonStyle2} />
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
    position: 'absolute', // Posiciona o título absolutamente
    top: 0, // Fixa no topo
    width: '100%',
    alignItems: 'center',
    marginTop: 80, // Distância do topo
  },
  textContainer: {
    position: 'absolute', // Posiciona o título absolutamente
    top: 0, // Fixa no topo
    width: '100%',
    alignItems: 'center',
    marginTop: 180, // Distância do topo
  },
  title: {
    fontSize: 24, // Tamanho do título
    fontWeight: 'bold', // Negrito
    marginBottom: 20, // Espaçamento inferior
  },
  buttonContainer: {
    width: '80%',
    justifyContent: 'center',
    marginTop: 100,
  },
  buttonStyle2: {
    marginTop: 20,
    height: 60,
    backgroundColor: '#F3AC3D',
    borderRadius: 12,
  },
  input: {
    height: 50,
    marginTop: 10,
  }
});
