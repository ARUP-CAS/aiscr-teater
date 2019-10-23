import React, { useState, FC, useEffect } from 'react'
import gql from 'graphql-tag'
import { useQuery } from 'react-apollo-hooks'
import Search from 'components/search'
import { FormResult } from 'components/search/_types'
import { useDebounce } from 'hooks/useDebounce'
import { history } from 'router/history'
import { URL_SEARCH } from 'constants/URL'
import { createURL } from 'utils/createURL'
import { useCountdown } from 'hooks/useCountdown'
import { GetSearchResults } from '../generated/GetSearchResults'

const GET_SEARCH_RESULTS = gql`
  query GetSearchResults($value: String!, $limit: Int) {
    search(value: $value, limit: $limit) {
      name
      url
      searchType
    }
  }
`

function useSuggestedData(
  value: string,
  limit: number
): { data: FormResult[]; loading: boolean; error: boolean } {
  const debouncedValue = useDebounce<string>(value, 150)
  const { data, loading, error } = useQuery<GetSearchResults>(
    GET_SEARCH_RESULTS,
    {
      variables: { value: debouncedValue, limit },
      skip: !debouncedValue
    }
  )

  return { data: data ? data.search : [], loading, error: !!error }
}

export const SearchContainer: FC<{ numberOfResults?: number }> = ({
  numberOfResults = 10
}) => {
  const [searchValue, setSearchValue] = useState('')
  const { data, loading, error } = useSuggestedData(
    searchValue,
    numberOfResults
  )
  const [whispererVisible, setWhispererVisible] = useState(false)
  const {
    isCountdownActivate: visibleError,
    activate: displayError,
    deactivate: hideError
  } = useCountdown(800)

  useEffect(
    () => {
      if (error) {
        displayError()
      }
    },
    [error, displayError]
  )

  function onSearchChange(event: React.ChangeEvent<HTMLInputElement>): void {
    setSearchValue(event.target.value)
    setWhispererVisible(true)
    if (visibleError) {
      hideError()
    }
  }

  function hideWhisperer(): void {
    setWhispererVisible(false)
  }

  function onSubmit(event: React.FormEvent<HTMLFormElement>): boolean {
    event.preventDefault()
    if (!searchValue) {
      displayError()
      return false
    }

    history.push(createURL(URL_SEARCH, encodeURIComponent(searchValue)))
    hideWhisperer()
    return false
  }

  function onFocus(): void {
    setWhispererVisible(true)
  }

  function onBlur(relatedTargetIsChildren: boolean): void {
    if (!relatedTargetIsChildren) {
      setWhispererVisible(false)
    }
  }

  return (
    <Search
      data={data}
      error={visibleError}
      value={searchValue}
      onChange={onSearchChange}
      onSubmit={onSubmit}
      onFocus={onFocus}
      onBlur={onBlur}
      loading={loading}
      whispererVisible={whispererVisible}
      hideWhisperer={hideWhisperer}
    />
  )
}
