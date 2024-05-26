import React, { useState, useContext } from 'react';
import { Text, View, StyleSheet } from 'react-native';
import { Button, Input } from 'react-native-elements';
import { Image } from 'react-native';
import { AuthContext } from './AuthProvider'; // Importa o AuthContext
import { useRouter } from 'expo-router'; // Importa useRouter do Expo Router

export default function LoginScreen() {
  const { login } = useContext(AuthContext); // Usa o contexto de autenticação
  const router = useRouter(); // Obtém a função de redirecionamento

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);

  const handleLogin = async () => {
    try {
      // Enviar solicitação POST para o endpoint de login
      const response = await fetch('http://localhost:8000/login/', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      });

      // Verificar se a resposta do servidor é bem-sucedida
      if (response.ok) {
        router.push('screens/home');
      } else {
        // Se a resposta do servidor não for bem-sucedida, exiba uma mensagem de erro
        console.error('Erro ao fazer login:', response.statusText);
      }
    } catch (error) {
      // Se ocorrer um erro durante a solicitação, exiba uma mensagem de erro
      console.error('Erro ao fazer login:', (error as Error).message);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.titleContainer}>
        <Image source={require('@/assets/images/boraja_logo.jpg')} style={{ alignSelf: 'center', width: 230, height: 60 }} />
      </View>
      <View style={styles.textContainer}>
        <Text style={styles.title}>Faça o login</Text>
      </View>
      <View style={styles.inputContainer}>
        <Input
          placeholder='E-mail'
          onChangeText={(text) => setEmail(text)}
          value={email}
        />
        <Input
          placeholder='Senha'
          onChangeText={(text) => setPassword(text)}
          value={password}
          secureTextEntry
        />
        {error && <Text style={styles.errorText}>{error}</Text>}
        <Button
          title="Login"
          onPress={handleLogin}
          buttonStyle={styles.buttonStyle}
        />
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
    marginTop: 80,
  },
  textContainer: {
    position: 'absolute',
    top: 0,
    width: '100%',
    alignItems: 'center',
    marginTop: 180,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  inputContainer: {
    width: '80%',
    marginTop: 100,
  },
  buttonStyle: {
    height: 50,
    backgroundColor: '#F3AC3D',
    borderRadius: 30,
  },
  errorText: {
    color: 'red',
    marginBottom: 10,
    textAlign: 'center',
  },
});
