import * as React from 'react';
import * as WebBrowser from 'expo-web-browser';
import { makeRedirectUri, useAuthRequest, useAutoDiscovery } from 'expo-auth-session';
import { Button, Text, View } from 'react-native';

WebBrowser.maybeCompleteAuthSession();

export default function LoginScreen() {
  const discovery = useAutoDiscovery('https://keycloak-production-f04b.up.railway.app/realms/boraja');

  // Create and load an auth request
  const [request, result, promptAsync] = useAuthRequest(
    {
      clientId: 'boraja-client',
      redirectUri: makeRedirectUri({
        scheme: 'myapp'
      }),
      scopes: ['openid', 'profile'],
    },
    discovery
  );

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Button title="Login!" disabled={!request} onPress={() => promptAsync()} />
      {result && <Text>{JSON.stringify(result, null, 2)}</Text>}
    </View>
  );
}