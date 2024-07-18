import React from 'react';
import { View, Text, StyleSheet, Image } from 'react-native';
import { useRouter, useLocalSearchParams } from 'expo-router';
import { Button, Icon } from 'react-native-elements';

export default function Profile() {
  const router = useRouter();
  const { token } = useLocalSearchParams();
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Perfil</Text>
      <View style={styles.buttonSpacer} />
      <Image
        style={styles.profileImage}
        source={{ uri: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQMggZhOIH1vXmnv0bCyBu8iEuYQO-Dw1kpp7_v2mwhw_SKksetiK0e4VWUak3pm-v-Moc&usqp=CAU' }}
      />
      <Text style={styles.name}>Olá, user.name </Text>
      <Text style={styles.stars}>4.5 <Icon name="star" size={15} color="black"/></Text>
      <View style={styles.buttonContainer}>
        <Button title="Editar Perfil" onPress={() => router.push('editProfile')} buttonStyle={styles.buttonStyle2} />
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
    paddingTop: 150, // Adicionei paddingTop para garantir que o título fique no topo
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 10,
    position: 'absolute', // Adicionei position absolute
    top: 30, // Ajuste a posição conforme necessário
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
