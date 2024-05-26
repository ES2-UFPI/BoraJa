import React, { useState, useContext } from 'react';
import { Text, View, StyleSheet } from 'react-native';
import { Button, Input } from 'react-native-elements';
import { Image } from 'react-native';
import { AuthContext } from '../AuthProvider';
import { useRouter } from 'expo-router'; // Importa useRouter do Expo Router
import * as ImagePicker from 'expo-image-picker';

export default function RegisterScreen() {
    const { login } = useContext(AuthContext); // Usa o contexto de autenticação
    const router = useRouter(); // Obtém a função de redirecionamento

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [nome, setNome] = useState('');
    const [cpf, setCpf] = useState('');
    const [nascimento, setNascimento] = useState('');
    const [image, setImage] = useState('');
    const [error, setError] = useState(null);

    const pickImage = async () => {
        // No permissions request is necessary for launching the image library
        let result = await ImagePicker.launchImageLibraryAsync({
          mediaTypes: ImagePicker.MediaTypeOptions.All,
          allowsEditing: true,
          aspect: [4, 3],
          quality: 1,
        });
    
        console.log(result);
    
        if (!result.canceled) {
          setImage(result.assets[0].uri);
        }
      };

    const handleCadastro = async () => {
        try {
            // Enviar solicitação POST para o endpoint de Cadastro
            // const response = await fetch('http://localhost:8000/cadastrar/', {
            //   method: 'POST',
            //   headers: {
            //     'Content-Type': 'application/json',
            //   },
            //   body: JSON.stringify({ email, password }),
            // });

            // // Verificar se a resposta do servidor é bem-sucedida
            // if (response.ok) {
            //   router.push('screens/index');
            if (password == '1234' && email == 'admin') {
                router.push('/'); // Redireciona para a tela de login
            } else {
                // Se a resposta do servidor não for bem-sucedida, exiba uma mensagem de erro
                console.error('Erro ao fazer cadastro:');
            }
        } catch (error) {
            // Se ocorrer um erro durante a solicitação, exiba uma mensagem de erro
            console.error('Erro ao fazer cadastro:', (error as Error).message);
        }
    };

    return (
        <View style={styles.container}>
            <View style={styles.titleContainer}>
                <Image source={require('@/assets/images/boraja_logo.jpg')} style={{ alignSelf: 'center', width: 230, height: 60 }} />
            </View>
            <View style={styles.textContainer}>
                <Text style={styles.title}>Faça o cadastro</Text>
            </View>
            <View style={styles.inputContainer}>
                <Input
                    placeholder='Nome completo'
                    onChangeText={(text) => setNome(text)}
                    value={nome}
                />
                <Input
                    placeholder='CPF'
                    onChangeText={(text) => setCpf(text)}
                    value={cpf}
                    secureTextEntry
                />
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
                <Input
                    placeholder='Data de Nascimento'
                    onChangeText={(text) => setNascimento(text)}
                    value={nascimento}
                    secureTextEntry
                />
                <Button
                    title="Escolher foto"
                    onPress={pickImage}
                    buttonStyle={styles.buttonStyle2}
                />
                <View style={styles.buttonSpacer} /> 
                {error && <Text style={styles.errorText}>{error}</Text>}
                <Button
                    title="Cadastrar"
                    onPress={handleCadastro}
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
        top: 0,
        width: '100%',
        alignItems: 'center',
    },
    textContainer: {
        top: 0,
        width: '100%',
        alignItems: 'center',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
    },
    inputContainer: {
        width: '80%',
        marginTop: 50,
    },
    buttonStyle: {
        height: 50,
        backgroundColor: '#F3AC3D',
        borderRadius: 30,
    },
    buttonStyle2: {
        height: 50,
        backgroundColor: '#5271FF',
        borderRadius: 30,
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
