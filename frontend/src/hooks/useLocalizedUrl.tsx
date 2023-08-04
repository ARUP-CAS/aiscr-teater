import { useCallback } from 'react';
import { useLocation } from 'react-router';

const useLocalizedUrl = () => {
	const { search } = useLocation();
	return useCallback((url: string) => `/${url}${search}`, [search]);
};
export default useLocalizedUrl;
