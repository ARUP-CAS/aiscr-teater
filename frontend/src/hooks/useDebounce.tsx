import { useState, useEffect } from 'react';

export const useDebounce = <T extends unknown>(value: T, delay: number) => {
  const [debouncedValue, setDebouncedValue] = useState<T>(value);

  useEffect(
    () => {
      const handler = setTimeout((): void => {
        setDebouncedValue(value);
      }, delay);

      return (): void => {
        clearTimeout(handler);
      };
    },
    [value, delay]
  );

  return debouncedValue;
};
