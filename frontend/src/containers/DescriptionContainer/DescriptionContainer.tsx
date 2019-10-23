import React, { FC, useState, useEffect, useRef } from 'react'
import { useQuery } from 'react-apollo-hooks'
import { ApolloError } from 'apollo-client'
import Breadcrumbs from 'components/breadcrumbs'
import Description from 'components/description'
import { GetDescriptionPage } from 'generated/GetDescriptionPage'
import { GET_DESCRIPTION_PAGE } from './_gql'
import {
  parseBreadcrumbsData,
  parseNeighborsData,
  parseChilrenData,
  parseParentsData
} from './_actions'
import { Ooops404 } from '../../components/Ooops'
import { ColumnItem } from '../../components/column/_types'
import { HeaderTags } from 'components/header'
import { isOutOfViewport } from 'utils/isOutOfViewport'

function useDescriptionPageData(
  isLoading: boolean,
  data?: GetDescriptionPage,
  error?: ApolloError
): {
  neighbors: ColumnItem[]
  breadcrumbs: ColumnItem[]
  children: ColumnItem[]
} {
  const [breadcrumbs, setBreadcrumbs] = useState(
    error ? [] : parseBreadcrumbsData(data)
  )
  const [recommendedChildren, setChildren] = useState(
    error ? [] : parseChilrenData(data)
  )
  const [recommendedNeighbors, setNeighbors] = useState(
    error ? [] : parseNeighborsData(data)
  )

  useEffect(
    () => {
      if (isLoading || error) {
        return
      }

      setBreadcrumbs(parseBreadcrumbsData(data))

      const neighbors = parseNeighborsData(data)
      const children = parseChilrenData(data)
      const parents = parseParentsData(data)

      // when is node leaf, try to display 2 columns
      if (!children.length && parents.length) {
        setNeighbors(parents)
        setChildren(neighbors)
      } else {
        setNeighbors(neighbors)
        setChildren(children)
      }
    },
    [isLoading, error, data]
  )

  return {
    neighbors: recommendedNeighbors,
    breadcrumbs,
    children: recommendedChildren
  }
}

export const DescriptionContainer: FC<{ pageUrl: string }> = ({ pageUrl }) => {
  const { data, loading, error } = useQuery<GetDescriptionPage>(
    GET_DESCRIPTION_PAGE,
    {
      variables: { url: pageUrl }
    }
  )
  const { breadcrumbs, neighbors, children } = useDescriptionPageData(
    loading,
    data,
    error
  )

  const titleRef = useRef<HTMLHeadingElement>(null)

  useEffect(
    () => {
      if (
        !loading &&
        titleRef.current &&
        isOutOfViewport(titleRef.current).any
      ) {
        titleRef.current.scrollIntoView()
      }
    },
    [loading, data]
  )

  if (error) {
    return <Ooops404 />
  }

  return (
    <>
      <HeaderTags
        title={data ? data.singleCategoryWithUrl.name : 'Načíta se'}
      />
      <Breadcrumbs data={breadcrumbs} />
      <Description
        ref={titleRef}
        columns={[
          {
            key: 'neighbors-column',
            data: neighbors
          },
          {
            key: 'children-column',
            data: children
          }
        ]}
        loading={loading}
        title={data ? data.singleCategoryWithUrl.name : ''}
        description={data ? data.singleCategoryWithUrl.description : ''}
      />
    </>
  )
}
