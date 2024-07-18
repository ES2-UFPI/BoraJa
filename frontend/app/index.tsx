import * as React from 'react';
import * as WebBrowser from 'expo-web-browser';
import { makeRedirectUri, useAuthRequest, useAutoDiscovery, ResponseType } from 'expo-auth-session';
import { Button, Text, View, ActivityIndicator, StyleSheet, TouchableOpacity } from 'react-native';
import { useRouter } from 'expo-router';
import { Image } from 'react-native';
import { Icon } from 'react-native-elements';

WebBrowser.maybeCompleteAuthSession();

export default function LoginScreen({ navigation }: { navigation: any }) {
  const router = useRouter();
  const discovery = useAutoDiscovery('https://keycloak-production-f04b.up.railway.app/realms/boraja');

  const [loading, setLoading] = React.useState(false);

  const [request, result, promptAsync] = useAuthRequest(
    {
      clientId: 'boraja-client',
      scopes: ['openid', 'profile'],
      redirectUri: makeRedirectUri({
        native: 'myapp://app/screens/home', // Verifique se esta URL está configurada corretamente no Keycloak
      }),
      responseType: ResponseType.Token,
    },
    discovery
  );

  React.useEffect(() => {
    if (result) {
      if (result.type === 'success') {
        const { access_token } = result.params;
        // Use the token to authenticate with your backend or navigate to the home screen
        router.push({ pathname: 'screens/home', params: { token: access_token } });
      } else {
        // Handle other result types or errors
        console.log('Authentication failed', result);
      }
      setLoading(false);
    }
  }, [result, navigation]);

  const handleLogin = async () => {
    setLoading(true);
    await promptAsync();
  };

  return (
    <View style={styles.container}>
      <View style={styles.titleContainer}>
        <Image source={require('@/assets/images/boraja_logo.jpg')} style={{ alignSelf: 'center', width: 250, height: 60 }} />
      </View>
      <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
        <TouchableOpacity
          style={{ flexDirection: 'row', alignItems: 'center' }}
          onPress={handleLogin}
        >
          <Text style={{ color: 'white', borderRadius: 12, backgroundColor: '#F3AC3D', padding: 15, paddingHorizontal: 30, fontSize: 18, fontWeight: 'bold', alignItems: 'center', display: 'flex', flexDirection: 'row' }}>Acessar sua conta</Text>
        </TouchableOpacity>
        {loading && <ActivityIndicator size="large" color="#0000ff" />}
        {result && <Text>{JSON.stringify(result, null, 2)}</Text>}
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
    marginTop: 200,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  buttonContainer: {
    width: '80%',
    justifyContent: 'center',
    marginTop: 100,
  },
  buttonStyle1: {
    height: 60,
    backgroundColor: 'black',
    borderRadius: 12,
  },
  buttonStyle2: {
    height: 60,
    backgroundColor: '#F3AC3D',
    borderRadius: 12,
  },
  buttonSpacer: {
    height: 30,
  },
});
