import React, { useState, useContext } from 'react';
import { Text, View, StyleSheet } from 'react-native';
import { Button, Input } from 'react-native-elements';
import { Image } from 'react-native';
import { AuthContext } from './AuthProvider';
import { useRouter } from 'expo-router';
import Icon from 'react-native-vector-icons/FontAwesome';

export default function LoginScreen() {
  const { login } = useContext(AuthContext);
  const router = useRouter();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);

  const handleLogin = async () => {
    if (!email || !password) {
      setError('Por favor, preencha todos os campos.');
      return;
    }

    try {
      if (password === '1234' && email === 'admin') {
        router.push('screens/home');
      } else {
        setError('E-mail ou senha incorretos.');
      }
    } catch (error) {
      console.error('Erro ao fazer login:', (error as Error).message);
      setError('Erro ao fazer login. Tente novamente.');
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.titleContainer}>
        <Image source={require('@/assets/images/boraja_logo.jpg')} style={{ alignSelf: 'center', width: 250, height: 60 }} />
      </View>
  
      <View style={styles.inputContainer}>
        <Input
          placeholder='E-mail'
          onChangeText={(text) => setEmail(text)}
          value={email}
          leftIcon={<Icon name='envelope' size={18} color='#999' />}
          leftIconContainerStyle={{ marginRight: 10 }}
          inputStyle={{color: '#777'}}
        />
        <Input
          placeholder='Senha'
          onChangeText={(text) => setPassword(text)}
          value={password}
          secureTextEntry
          leftIcon={<Icon name='lock' size={24} color='#999' />}
          leftIconContainerStyle={{ marginRight: 10 }}
          inputStyle={{color: '#777'}}
        />
        {error && <Text style={styles.errorText}>{error}</Text>}
        <Button
          title="Login"
          onPress={handleLogin}
          buttonStyle={styles.buttonStyle}
        />
        <View style={styles.textContainer}>
          <Text onPress={() => router.push('screens/register')}>Não tem cadastro ainda? <Text style={styles.registerText}>Cadastrar</Text></Text>
        </View>
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
    top: 0,
    width: '100%',
    alignItems: 'center',
    marginTop: 70,
  },
  textContainer: {
    top: 0,
    width: '100%',
    alignItems: 'center',
    marginTop: 10,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  registerText: {
    color: '#5271FF',
  },
  inputContainer: {
    width: '80%',
    marginTop: 70,
  },
  buttonStyle: {
    height: 50,
    backgroundColor: '#F3AC3D',
    borderRadius: 12,
    marginTop: 15,
  },
  errorText: {
    color: 'red',
    marginBottom: 10,
    textAlign: 'center',
  },
  buttonSpacer: {
    height: 30,
  }
});
