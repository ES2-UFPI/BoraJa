import React from 'react';
import { render, fireEvent, waitFor } from '@testing-library/react-native';
import PassageiroScreen from '../../app/passageiro/homePassageiro';
import { useLocalSearchParams } from 'expo-router';
import * as Location from 'expo-location';

jest.mock('expo-router', () => ({
  useLocalSearchParams: jest.fn(),
}));

jest.mock('expo-location', () => ({
  requestForegroundPermissionsAsync: jest.fn(),
  getCurrentPositionAsync: jest.fn(),
}));

jest.mock('@react-navigation/native', () => ({
  useNavigation: jest.fn(),
}));

describe('PassageiroScreen', () => {
  beforeEach(() => {
    (useLocalSearchParams as jest.Mock).mockReturnValue({ token: 'test-token' });
    (Location.requestForegroundPermissionsAsync as jest.Mock).mockResolvedValue({ granted: true });
    (Location.getCurrentPositionAsync as jest.Mock).mockResolvedValue({
      coords: {
        latitude: 37.78825,
        longitude: -122.4324,
      },
    });
  });

  test('renders the Map component', async () => {
    const { getByText } = render(<PassageiroScreen />);

    await waitFor(() => expect(getByText('Você está aqui')).toBeTruthy());
  });

  test('opens modal when "Buscar Carona" button is pressed', async () => {
    const { getByText } = render(<PassageiroScreen />);

    fireEvent.press(getByText('Buscar Carona'));

    await waitFor(() => expect(getByText('Viagens Disponíveis')).toBeTruthy());
  });

  test('renders trip items when trips are available', async () => {
    global.fetch = jest.fn().mockResolvedValueOnce({
      json: jest.fn().mockResolvedValueOnce([
        {
          id: 1,
          motoristaNome: 'João',
          veiculoPlaca: 'ABC-1234',
          origem: { nome: 'São Paulo' },
          destino: { nome: 'Rio de Janeiro' },
          previsaoSaida: '2024-08-08 08:00:00',
          quantidadeVagas: 3,
        },
      ]),
    });

    const { getByText } = render(<PassageiroScreen />);

    fireEvent.press(getByText('Buscar Carona'));

    await waitFor(() => expect(getByText('Motorista: João')).toBeTruthy());
    expect(getByText('Veículo: ABC-1234')).toBeTruthy();
    expect(getByText('Partida: São Paulo')).toBeTruthy();
    expect(getByText('Chegada: Rio de Janeiro')).toBeTruthy();
  });
});
