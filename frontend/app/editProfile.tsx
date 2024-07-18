import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

export default function editProfile() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Editar Perfil</Text>
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
});
