import React from 'react';
import { render, fireEvent, waitFor } from '@testing-library/react-native';
import MotoristaScreen from '../../app/motorista/homeMotorista'; // Corrigido o nome da importação
import * as Location from 'expo-location';
import Geocoder from 'react-native-geocoding';

jest.mock('react-native-maps', () => {
  const { View } = require('react-native');
  return {
    __esModule: true,
    default: jest.fn(),
    Marker: jest.fn(),
  };
});

jest.mock('react-native-geocoding', () => ({
  from: jest.fn(),
  init: jest.fn(),
}));

jest.mock('expo-location', () => ({
  requestForegroundPermissionsAsync: jest.fn(),
  getCurrentPositionAsync: jest.fn(),
}));

jest.mock('expo-router', () => ({
  useRouter: jest.fn(() => ({
    push: jest.fn(),
  })),
  useLocalSearchParams: jest.fn(() => ({
    token: 'mock-token',
  })),
}));

jest.mock('jwt-decode', () => jest.fn(() => ({
  preferred_username: 'mock-username',
})));

describe('MotoristaScreen', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders correctly', async () => {
    (Location.requestForegroundPermissionsAsync as jest.Mock).mockResolvedValue({ granted: true });
    (Location.getCurrentPositionAsync as jest.Mock).mockResolvedValue({
      coords: { latitude: 0, longitude: 0 },
    });

    const { getByText, getByPlaceholderText } = render(<MotoristaScreen />);
    
    // Verifica se os componentes principais estão sendo renderizados
    await waitFor(() => {
      expect(getByText('Iniciar carona')).toBeTruthy();
    });

    // Simula a abertura do modal
    fireEvent.press(getByText('Iniciar carona'));
    await waitFor(() => {
      expect(getByText('Iniciar Nova Viagem')).toBeTruthy();
    });

    // Simula a inserção de dados no formulário do modal
    fireEvent.changeText(getByPlaceholderText('Quantidade de Vagas'), '3');
    fireEvent.changeText(getByPlaceholderText('Placa do Veículo'), 'ABC-1234');

    // Verifica se os dados foram inseridos corretamente
    expect(getByPlaceholderText('Quantidade de Vagas').props.value).toBe('3');
    expect(getByPlaceholderText('Placa do Veículo').props.value).toBe('ABC-1234');
  });

  it('handles location permissions correctly', async () => {
    (Location.requestForegroundPermissionsAsync as jest.Mock).mockResolvedValue({ granted: true });
    (Location.getCurrentPositionAsync as jest.Mock).mockResolvedValue({
      coords: { latitude: 0, longitude: 0 },
    });

    render(<MotoristaScreen />);

    await waitFor(() => {
      expect(Location.requestForegroundPermissionsAsync).toHaveBeenCalled();
      expect(Location.getCurrentPositionAsync).toHaveBeenCalled();
    });
  });

  it('handles address submission', async () => {
    (Location.requestForegroundPermissionsAsync as jest.Mock).mockResolvedValue({ granted: true });
    (Location.getCurrentPositionAsync as jest.Mock).mockResolvedValue({
      coords: { latitude: 0, longitude: 0 },
    });
    (Geocoder.from as jest.Mock).mockResolvedValue({
      results: [{
        geometry: { location: { lat: 1, lng: 1 } },
        formatted_address: 'Mock Address',
      }],
    });

    const { getByPlaceholderText, getByDisplayValue } = render(<MotoristaScreen />);

    // Simula a inserção de um endereço e a submissão
    fireEvent.changeText(getByPlaceholderText('Endereço do Destino'), 'Mock Address');
    fireEvent(getByPlaceholderText('Endereço do Destino'), 'submitEditing', {
      nativeEvent: { text: 'Mock Address' },
    });

    await waitFor(() => {
      expect(getByDisplayValue('Mock Address')).toBeTruthy();
    });
  });
});
