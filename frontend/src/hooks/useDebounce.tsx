import { useState, useEffect } from 'react'

export function useDebounce<T>(value: T, delay: number): T {
  const [debouncedValue, setDebouncedValue] = useState<T>(value)

  useEffect(
    () => {
      const handler = setTimeout((): void => {
        setDebouncedValue(value)
      }, delay)

      return (): void => {
        clearTimeout(handler)
      }
    },
    [value, delay]
  )

  return debouncedValue
}
