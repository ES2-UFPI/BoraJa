import { Text, View, StyleSheet } from 'react-native';
import { Button, Input } from 'react-native-elements';
import { Image } from 'react-native';

export default function PassengerScreen() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Mapa</Text>
      <Button title="Buscar carona" buttonStyle={styles.buttonStyle2} />
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
    marginTop: 200,
  },
  buttonStyle2: {
    height: 60,
    borderRadius: 12,
    marginTop: 300,
    width: 250,
  },
});
