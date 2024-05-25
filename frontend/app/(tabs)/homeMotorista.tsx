import { Text, View, StyleSheet } from 'react-native';
import { Button } from 'react-native-elements';

export default function HomeScreen() {
  return (
    <View style={styles.container}>
      <View style={styles.titleContainer}>
        <Text style={styles.title}>Início</Text>
      </View>
      <View style={styles.buttonContainer}>
        <Button title="Buscar Viagem" onPress={() => console.log("Button 1 pressed")} buttonStyle={styles.buttonStyle1} />
        <View style={styles.buttonSpacer} /> 
        <Button title="Iniciar Viagem" onPress={() => console.log("Button 2 pressed")} buttonStyle={styles.buttonStyle2} />
        <View style={styles.buttonSpacer} /> 
        <Button title="Minhas Viagens" onPress={() => console.log("Button 3 pressed")} buttonStyle={styles.buttonStyle1} />
        <View style={styles.buttonSpacer} /> 
        <Button title="Meu Saldo" onPress={() => console.log("Button 4 pressed")} buttonStyle={styles.buttonStyle2} />
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
    marginTop: 50, // Distância do topo
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
  buttonStyle1: {
    height: 50, // Define a altura dos botões
    backgroundColor: 'black', // Cor de fundo dos botões
    borderRadius: 30, // Borda arredondada
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
