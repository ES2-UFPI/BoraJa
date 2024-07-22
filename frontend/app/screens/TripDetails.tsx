import { Text, View, StyleSheet, Button } from 'react-native';
import { useRoute } from '@react-navigation/native';
import { useState, useEffect } from 'react';
import { useLocalSearchParams } from 'expo-router';

// Define TypeScript types for the trip and participant objects
interface Trip {
  horarioSaida: string;
  selectedVehicle: string;
  quantidadeVagas: number;
  origem: { nome: string };
  destino: { nome: string };
}

interface Participant {
  id: string;
  nome: string;
}

export default function TripDetails() {
  const [trip, setTrip] = useState<Trip | null>(null);
  const [participants, setParticipants] = useState<Participant[]>([]);
  const { token } = useLocalSearchParams();
  const route = useRoute();

  // Type assertion for route.params
  const tripId = (route.params as { tripId: string }).tripId;

  useEffect(() => {
    const fetchTripDetails = async () => {
      try {
        const response = await fetch(`http://26.78.193.223:8085/viagem/search?id=${tripId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (!response.ok) {
          throw new Error('Failed to fetch trip details');
        }
        const data = await response.json();
        setTrip(data);
      } catch (error) {
        console.error('Erro ao buscar detalhes da viagem:', error);
      }
    };

    const fetchParticipants = async () => {
      try {
        const response = await fetch(`http://26.78.193.223:8085/viagem/${tripId}/participantes`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (!response.ok) {
          throw new Error('Failed to fetch participants');
        }
        const data = await response.json();
        setParticipants(data);
      } catch (error) {
        console.error('Erro ao buscar participantes:', error);
      }
    };

    fetchTripDetails();
    fetchParticipants();

    const interval = setInterval(() => {
      fetchParticipants();
    }, 5000); // Atualiza a lista de participantes a cada 5 segundos

    return () => clearInterval(interval);
  }, [tripId, token]);

  const handleStartTrip = async () => {
    try {
      const response = await fetch(`http://26.78.193.223:8085/viagem/iniciar/${tripId}`, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.ok) {
        console.log('Viagem iniciada com sucesso!');
        // Optionally redirect or update the UI here
      } else {
        console.error('Erro ao iniciar viagem:', response.status);
      }
    } catch (error) {
      console.error('Erro ao iniciar viagem:', error);
    }
  };

  if (!trip) {
    return (
      <View style={styles.container}>
        <Text>Carregando detalhes da viagem...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Detalhes da Viagem</Text>
      <View style={styles.details}>
        <Text>Horário de Saída: {trip.horarioSaida}</Text>
        <Text>Veículo: {trip.selectedVehicle}</Text>
        <Text>Quantidade de Vagas: {trip.quantidadeVagas}</Text>
        <Text>Origem: {trip.origem.nome}</Text>
        <Text>Destino: {trip.destino.nome}</Text>
      </View>
      <Text style={styles.subtitle}>Participantes:</Text>
      <View style={styles.participants}>
        {participants.length > 0 ? (
          participants.map((participant) => (
            <Text key={participant.id}>{participant.nome}</Text>
          ))
        ) : (
          <Text>Nenhum participante encontrado.</Text>
        )}
      </View>
      <Button
        title="Iniciar Viagem"
        onPress={handleStartTrip}
        disabled={participants.length < trip.quantidadeVagas}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  details: {
    marginBottom: 20,
  },
  subtitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  participants: {
    marginBottom: 20,
  },
});
