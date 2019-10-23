import React, { FC } from 'react'
import gql from 'graphql-tag'
import { useQuery } from 'react-apollo-hooks'
import { RouteComponentProps } from 'react-router-dom'
import 'scss/pages/_page_search.scss'
import { Page } from 'components/Page'
import { Content } from 'components/Content'
import { SearchResultsContainer } from 'containers/SearchResultsContainer'
import { GetSearchPage } from 'generated/GetSearchPage'
import Categories from 'components/categories'
import { OoopsError } from '../components/Ooops'
import { SmallHeader } from '../components/SmallHeader'

const GET_SEARCH_PAGE = gql`
  query GetSearchPage($value: String!) {
    search(value: $value) {
      name
      url
      searchType
    }
    listRootCategories {
      id
      name
      url
    }
  }
`

const SearchPage: FC<RouteComponentProps<{ searchValue?: string }>> = ({
  match: {
    params: { searchValue }
  }
}) => {
  const utilizedSearchValue = decodeURIComponent(searchValue || '')
  const { data, loading, error } = useQuery<GetSearchPage>(GET_SEARCH_PAGE, {
    variables: { value: searchValue }
  })

  if (error) {
    return (
      <Page>
        <SmallHeader title={searchValue} />
        <OoopsError />
      </Page>
    )
  }

  return (
    <Page>
      <SmallHeader title={searchValue} />
      <Content className="content-search">
        <h3>
          Výsledky pro výraz:{' '}
          <span className="meta">{utilizedSearchValue}</span>
        </h3>
        <SearchResultsContainer
          data={data ? data.search : []}
          loading={loading}
        />
        <div>
          <h4>Prozkoumejte další oblasti</h4>
          <Categories
            data={data ? data.listRootCategories : []}
            loading={loading}
          />
        </div>
      </Content>
    </Page>
  )
}

export default SearchPage
