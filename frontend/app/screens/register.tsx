import React, { useState, useContext } from 'react';
import { Text, View, StyleSheet } from 'react-native';
import { Button, Input } from 'react-native-elements';
import { Image } from 'react-native';
import { AuthContext } from '../AuthProvider';
import { useRouter } from 'expo-router';
import * as ImagePicker from 'expo-image-picker';
import Icon from 'react-native-vector-icons/FontAwesome';

export default function RegisterScreen() {
    const { login } = useContext(AuthContext);
    const router = useRouter();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [nome, setNome] = useState('');
    const [cpf, setCpf] = useState('');
    const [nascimento, setNascimento] = useState('');
    const [image, setImage] = useState('');
    const [error, setError] = useState<string | null>(null);

    const pickImage = async () => {
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

    const handleRegister = async () => {
        if (!nome || !cpf || !email || !password || !nascimento) {
            setError('Por favor, preencha todos os campos.');
            return;
        }

        try {
            if (password === '1234' && email === 'admin') {
                router.push('/');
            } else {
                setError('Erro ao fazer cadastro.');
            }
        } catch (error) {
            console.error('Erro ao fazer cadastro:', (error as Error).message);
            setError('Erro ao fazer cadastro. Tente novamente.');
        }
    };

    return (
        <View style={styles.container}>
            <View style={styles.titleContainer}>
                <Image source={require('@/assets/images/boraja_logo.jpg')} style={{ alignSelf: 'center', width: 250, height: 60 }} />
            </View>
        
            <View style={styles.inputContainer}>
                <Input
                    placeholder='Nome completo'
                    onChangeText={(text) => setNome(text)}
                    value={nome}
                    leftIcon={<Icon name='user' size={18} color='#999' />}
                    leftIconContainerStyle={{ marginRight: 10 }}
                    inputStyle={{color: '#777'}}
                />
                <Input
                    placeholder='CPF'
                    onChangeText={(text) => setCpf(text)}
                    value={cpf}
                    leftIcon={<Icon name='id-card' size={16} color='#999' />}
                    leftIconContainerStyle={{ marginRight: 10 }}
                    inputStyle={{color: '#777'}}
                />
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
                <Input
                    placeholder='Data de Nascimento'
                    onChangeText={(text) => setNascimento(text)}
                    value={nascimento}
                    leftIcon={<Icon name='calendar' size={19} color='#999' />}
                    leftIconContainerStyle={{ marginRight: 10 }}
                    inputStyle={{color: '#777'}}
                />
                <Button
                    title="Escolher foto"
                    onPress={pickImage}
                    buttonStyle={styles.buttonStyle2}
                    icon={<Icon name="camera" size={20} color="white" style={styles.iconStyle} />}
                    iconRight
                />
                <View style={styles.buttonSpacer} /> 
                {error && <Text style={styles.errorText}>{error}</Text>}
                <Button
                    title="Cadastrar"
                    onPress={handleRegister}
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
        borderRadius: 12,
    },
    buttonStyle2: {
        height: 50,
        backgroundColor: '#5271FF',
        borderRadius: 12,
        marginTop: 10,
    },
    iconStyle: {
        marginLeft: 12,
        alignSelf: 'center',
    },
    errorText: {
        color: 'red',
        marginBottom: 10,
        textAlign: 'center',
    },
    buttonSpacer: {
      height: 20,
    }
});
