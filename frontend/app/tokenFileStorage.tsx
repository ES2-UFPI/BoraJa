import * as FileSystem from 'expo-file-system';

const TOKEN_FILE_PATH = `${FileSystem.documentDirectory}authToken.txt`;

export const saveTokenToFile = async (token: string) => {
  try {
    await FileSystem.writeAsStringAsync(TOKEN_FILE_PATH, token);
  } catch (e) {
    console.error('Failed to save the token to file', e);
  }
};

export const getTokenFromFile = async () => {
  try {
    return await FileSystem.readAsStringAsync(TOKEN_FILE_PATH);
  } catch (e) {
    console.error('Failed to fetch the token from file', e);
    return null;
  }
};

export const removeTokenFromFile = async () => {
  try {
    await FileSystem.deleteAsync(TOKEN_FILE_PATH);
  } catch (e) {
    console.error('Failed to remove the token from file', e);
  }
};