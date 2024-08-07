import { Tabs } from 'expo-router';
import React from 'react';
import { TabBarIcon } from '@/components/navigation/TabBarIcon';
import { Colors } from '@/constants/Colors';
import { useColorScheme } from '@/hooks/useColorScheme';
import { useLocalSearchParams } from 'expo-router';

export default function TabLayout() {
  const colorScheme = useColorScheme();
  const { token } = useLocalSearchParams();

  return (
    <Tabs
      screenOptions={{
        tabBarActiveTintColor: Colors[colorScheme ?? 'light'].tint,
        headerShown: false,
      }}>
      <Tabs.Screen
        name="homePassageiro"
        initialParams={{ access_token: token }}
        options={{
          title: '',
          tabBarIcon: ({ color, focused }) => (
            <TabBarIcon name={focused ? 'home' : 'home-outline'} color='#444' />
          ),
        }}
      />
      <Tabs.Screen
        name="profile"
        initialParams={{ access_token: token }}
        options={{
          title: '',
          tabBarIcon: ({ color, focused }) => (
            <TabBarIcon name={focused ? 'person' : 'person-outline'} color='#444' />
          ),
        }}
      />
      <Tabs.Screen
        name="settings"
        initialParams={{ access_token: token }}
        options={{
          title: '',
          tabBarIcon: ({ color, focused }) => (
            <TabBarIcon name={focused ? 'cog' : 'cog-outline'} color='#444' />
          ),
        }}
      />
    </Tabs>
  );
}