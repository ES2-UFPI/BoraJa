import React from 'react';
import { TouchableOpacity, Text, StyleSheet, Image } from 'react-native';
import { useRouter } from 'expo-router';

const BackButton = () => {
  const router = useRouter();

  return (
    <TouchableOpacity style={styles.button} onPress={() => router.back()}>
      <Text style={styles.buttonText}> Voltar</Text>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  button: {
    position: 'absolute',
    top: 10,
    left: 10,
    backgroundColor: '#F3AC3D',
    padding: 10,
    borderRadius: 5,
    alignItems: 'center',
    marginBottom: 10,
    zIndex: 10
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
  },
});

export default BackButton;