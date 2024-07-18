import * as React from 'react';
import * as WebBrowser from 'expo-web-browser';
import { makeRedirectUri, useAuthRequest, useAutoDiscovery, ResponseType } from 'expo-auth-session';
import { Button, Text, View, ActivityIndicator } from 'react-native';
import { useRouter } from 'expo-router';

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
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Button title="Login!" disabled={!request || loading} onPress={handleLogin} />
      {loading && <ActivityIndicator size="large" color="#0000ff" />}
      {result && <Text>{JSON.stringify(result, null, 2)}</Text>}
    </View>
  );
}