import { Text, View, StyleSheet } from 'react-native';
import { Button, Input } from 'react-native-elements';
import { Image } from 'react-native'; // Import the Image component from react-native

export default function HomeScreen() {
  return (

    <View style={styles.container}>
      <View style={styles.titleContainer}>
        <Image source={require('@/assets/images/boraja_logo.jpg')} style={{ alignSelf: 'center', width: 230, height: 60 }} />
      </View>
      <View style={styles.textContainer}>
        <Text style={styles.title}>Faça o login</Text>
      </View>
      <View style={styles.buttonContainer}>
        <Input placeholder='E-mail' />
        <Input placeholder='Senha' />
        <Button title="Login" onPress={() => console.log("Button 4 pressed")} buttonStyle={styles.buttonStyle2} />
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
    width: '80%', // Define a largura do container de botões
    justifyContent: 'center', // Centraliza verticalmente
    marginTop: 100, // Espaçamento do topo para os botões
  },
  buttonStyle2: {
    height: 50, // Define a altura dos botões
    backgroundColor: '#F3AC3D', // Cor de fundo dos botões
    borderRadius: 30, // Borda arredondada
  },
  buttonSpacer: {
    height: 30, // Espaçamento entre os botões
  },
});
