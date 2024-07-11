import React, { useState } from 'react';
import { View, Text, StyleSheet, Modal, TouchableOpacity } from 'react-native';
import { AirbnbRating, Button } from 'react-native-elements';

interface ReviewPopupProps {
  visible: boolean;
  onClose: () => void;
}

export default function ReviewPopup({ visible, onClose }: ReviewPopupProps) {
  const [rating, setRating] = useState(0);

  const submitReview = () => {
    console.log(`Review submitted with rating: ${rating} stars`);
    onClose();
  };

  return (
    <Modal
      animationType="slide"
      transparent={true}
      visible={visible}
      onRequestClose={onClose}
    >
      <View style={styles.centeredView}>
        <View style={styles.modalView}>
          <Text style={styles.modalText}>Avalie sua experiência</Text>
          <AirbnbRating
            count={5}
            reviews={["Terrível", "Ruim", "Ok", "Bom", "Excelente"]}
            defaultRating={0}
            size={30}
            onFinishRating={(value) => setRating(value)}
          />
          <Button
            title="Enviar Avaliação"
            buttonStyle={styles.buttonStyle}
            onPress={submitReview}
          />
          <TouchableOpacity style={styles.closeButton} onPress={onClose}>
            <Text style={styles.textStyle}>Fechar</Text>
          </TouchableOpacity>
        </View>
      </View>
    </Modal>
  );
}

const styles = StyleSheet.create({
  centeredView: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0,0,0,0.5)',
  },
  modalView: {
    margin: 20,
    backgroundColor: 'white',
    borderRadius: 20,
    padding: 35,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 5,
  },
  modalText: {
    marginBottom: 15,
    textAlign: 'center',
    fontSize: 20,
    fontWeight: 'bold',
  },
  buttonStyle: {
    backgroundColor: '#F3AC3D',
    borderRadius: 12,
    marginTop: 15,
  },
  closeButton: {
    marginTop: 15,
  },
  textStyle: {
    color: '#5271FF',
    fontWeight: 'bold',
    textAlign: 'center',
  },
});