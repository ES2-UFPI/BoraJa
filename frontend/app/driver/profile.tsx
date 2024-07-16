import React from 'react';
import { View, Text, StyleSheet, Image } from 'react-native';
import { useRouter } from 'expo-router';
import { Button } from 'react-native-elements';


export default function Profile() {

  const router = useRouter();

  return (
    <View style={styles.container}>

      <Text style={styles.name}>Configurações </Text>
      <View style={styles.buttonSpacer} />
      <Image
        style={styles.profileImage}
        source={{ uri: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQMggZhOIH1vXmnv0bCyBu8iEuYQO-Dw1kpp7_v2mwhw_SKksetiK0e4VWUak3pm-v-Moc&usqp=CAU' }}
      />
      <Text style={styles.name}>Olá , </Text>
      
      <View style={styles.buttonContainer}>
        <Button title="Editar Perfil" onPress={() => router.push('driver/settings')} buttonStyle={styles.buttonStyle2} />
        <View style={styles.buttonSpacer} /> 
        <Button title="Ajuda" buttonStyle={styles.buttonStyle2}/>
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
    padding: 20,
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
  buttonStyle2: {
    height: 60,
    backgroundColor: '#00000;',
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
